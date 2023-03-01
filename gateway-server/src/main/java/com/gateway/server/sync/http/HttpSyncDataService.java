package com.gateway.server.sync.http;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gateway.server.concurrent.GatewayThreadFactory;
import com.gateway.server.constant.GatewayConstants;
import com.gateway.server.dto.ConfigData;
import com.gateway.server.enums.ConfigGroupEnum;
import com.gateway.server.exception.GatewayException;
import com.gateway.server.sync.AppDataSubscriber;
import com.gateway.server.sync.SyncDataService;
import com.gateway.server.sync.http.config.HttpConfig;
import com.gateway.server.sync.http.refresh.DataRefreshFactory;
import com.gateway.server.utils.ThreadUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class HttpSyncDataService implements SyncDataService {

    /**
     * logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(HttpSyncDataService.class);

    private static final AtomicBoolean RUNNING = new AtomicBoolean(false);

    /**
     * only use for http long polling.
     */
    private final RestTemplate restTemplate;

    private ExecutorService executor;

    private final List<String> serverList;

    private final DataRefreshFactory factory;

    private final AccessTokenManager accessTokenManager;

    public HttpSyncDataService(final HttpConfig httpConfig,
                               final RestTemplate restTemplate,
                               final List<AppDataSubscriber> appDataSubscribers,
                               final AccessTokenManager accessTokenManager) {
        this.accessTokenManager = accessTokenManager;
        this.factory = new DataRefreshFactory(appDataSubscribers);
        this.serverList = Lists.newArrayList(Splitter.on(",").split(httpConfig.getUrl()));
        this.restTemplate = restTemplate;
        this.start();
    }

    private void start() {
        // It could be initialized multiple times, so you need to control that.
        if (RUNNING.compareAndSet(false, true)) {
            // fetch all group configs.
            this.fetchGroupConfig(ConfigGroupEnum.values());
            int threadSize = serverList.size();
            this.executor = new ThreadPoolExecutor(threadSize, threadSize, 60L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(),
                    GatewayThreadFactory.create("http-long-polling", true));
            // start long polling, each server creates a thread to listen for changes.
            this.serverList.forEach(server -> this.executor.execute(new HttpLongPollingTask(server)));
        } else {
            LOG.info("shenyu http long polling was started, executor=[{}]", executor);
        }
    }

    private void fetchGroupConfig(final ConfigGroupEnum... groups) throws GatewayException {
        for (int index = 0; index < this.serverList.size(); index++) {
            String server = serverList.get(index);
            try {
                this.doFetchGroupConfig(server, groups);
                break;
            } catch (GatewayException e) {
                // no available server, throw exception.
                if (index >= serverList.size() - 1) {
                    throw e;
                }
                LOG.warn("fetch config fail, try another one: {}", serverList.get(index + 1));
            }
        }
    }

    private void doFetchGroupConfig(final String server, final ConfigGroupEnum... groups) {
        StringBuilder params = new StringBuilder();
        for (ConfigGroupEnum groupKey : groups) {
            params.append("groupKeys").append("=").append(groupKey.name()).append("&");
        }
        String url = server + GatewayConstants.SHENYU_ADMIN_PATH_CONFIGS_FETCH + "?" + StringUtils.removeEnd(params.toString(), "&");
        LOG.info("request configs: [{}]", url);
        String json;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(GatewayConstants.X_ACCESS_TOKEN, this.accessTokenManager.getAccessToken());
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            json = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class).getBody();
        } catch (RestClientException e) {
            String message = String.format("fetch config fail from server[%s], %s", url, e.getMessage());
            LOG.warn(message);
            throw new GatewayException(message, e);
        }
        // update local cache
        boolean updated = this.updateCacheWithJson(json);
        if (updated) {
            LOG.debug("get latest configs: [{}]", json);
            return;
        }
        // not updated. it is likely that the current config server has not been updated yet. wait a moment.
        LOG.info("The config of the server[{}] has not been updated or is out of date. Wait for 30s to listen for changes again.", server);
        ThreadUtils.sleep(TimeUnit.SECONDS, 30);
    }


    /**
     * update local cache.
     *
     * @param json the response from config server.
     * @return true: the local cache was updated. false: not updated.
     */
    private boolean updateCacheWithJson(final String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        // if the config cache will be updated?
        return factory.executor(jsonObject.getJSONObject("data"));
    }

    private void doLongPolling(final String server) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(8);
        for (ConfigGroupEnum group : ConfigGroupEnum.values()) {
            ConfigData<?> cacheConfig = factory.cacheConfigData(group);
            if (cacheConfig != null) {
                String value = String.join(",", cacheConfig.getMd5(), String.valueOf(cacheConfig.getLastModifyTime()));
                params.put(group.name(), Lists.newArrayList(value));
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(GatewayConstants.X_ACCESS_TOKEN, this.accessTokenManager.getAccessToken());
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        String listenerUrl = server + GatewayConstants.SHENYU_ADMIN_PATH_CONFIGS_LISTENER;

        JSONArray groupJson;
        try {
            String json = this.restTemplate.postForEntity(listenerUrl, httpEntity, String.class).getBody();
            LOG.info("listener result: [{}]", json);
            JSONObject responseFromServer = JSON.parseObject(json);
            groupJson = responseFromServer.getJSONArray("data");
        } catch (RestClientException e) {
            String message = String.format("listener configs fail, server:[%s], %s", server, e.getMessage());
            throw new GatewayException(message, e);
        }

        if (Objects.nonNull(groupJson) && groupJson.size() > 0) {
            // fetch group configuration async.
            ConfigGroupEnum[] changedGroups = groupJson.toArray(ConfigGroupEnum.class);
            LOG.info("Group config changed: {}", Arrays.toString(changedGroups));
            this.doFetchGroupConfig(server, changedGroups);
        }
    }

    @Override
    public void close() throws Exception {

    }

    class HttpLongPollingTask implements Runnable {

        private final String server;

        HttpLongPollingTask(final String server) {
            this.server = server;
        }

        @Override
        public void run() {
            while (RUNNING.get()) {
                int retryTimes = 3;
                for (int time = 1; time <= retryTimes; time++) {
                    try {
                        //do long polling.
                        doLongPolling(server);
                    } catch (Exception e) {
                        // print warnning LOG.
                        if (time < retryTimes) {
                            LOG.warn("Long polling failed, tried {} times, {} times left, will be suspended for a while! {}",
                                    time, retryTimes - time, e.getMessage());
                            ThreadUtils.sleep(TimeUnit.SECONDS, 5);
                            continue;
                        }
                        // print error, then suspended for a while.
                        LOG.error("Long polling failed, try again after 5 minutes!", e);
                        ThreadUtils.sleep(TimeUnit.MINUTES, 5);
                    }
                }
            }
            LOG.warn("Stop http long polling.");
        }
    }
}
