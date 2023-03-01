package com.gateway.server.sync.websocket;

import com.gateway.server.sync.*;
import com.gateway.server.sync.websocket.client.GatewawyWebsocketClient;
import com.gateway.server.sync.websocket.config.WebsocketConfig;
import com.gateway.server.utils.IpUtil;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class WebsocketSyncDataService implements SyncDataService {

    private static final Logger LOG = LoggerFactory.getLogger(WebsocketSyncDataService.class);

    private static final String ORIGIN_HEADER_NAME = "Origin";

    private final List<GatewawyWebsocketClient> clients = new ArrayList<>();

    /**
     * Instantiates a new Websocket sync cache.
     *
     * @param websocketConfig    the websocket config
     * @param appDataSubscribers the auth data subscribers
     * @param environment
     */
    public WebsocketSyncDataService(final WebsocketConfig websocketConfig,
                                    final List<AppDataSubscriber> appDataSubscribers,
                                    final List<RouteDataSubscriber> routeDataSubscribers,
                                    final List<UpstreamDataSubscriber> upstreamDataSubscribers,
                                    final List<SecretDataSubscriber> secretDataSubscribers,
                                    final Environment environment) {

        String host = IpUtil.getHost();
        String port = environment.getProperty("server.port");
        String[] urls = StringUtils.split(websocketConfig.getUrls(), ",");
        for (String url : urls) {
            try {
                URIBuilder builder = new URIBuilder(url);
                builder.addParameter("host", host);
                builder.addParameter("port", port);
                URI uri = builder.build();
                if (StringUtils.isNotEmpty(websocketConfig.getAllowOrigin())) {
                    Map<String, String> headers = ImmutableMap.of(ORIGIN_HEADER_NAME, websocketConfig.getAllowOrigin());
                    clients.add(new GatewawyWebsocketClient(uri,
                            headers,
                            appDataSubscribers,
                            routeDataSubscribers,
                            upstreamDataSubscribers,
                            secretDataSubscribers));
                } else {
                    clients.add(new GatewawyWebsocketClient(uri,
                            appDataSubscribers,
                            routeDataSubscribers,
                            upstreamDataSubscribers,
                            secretDataSubscribers));
                }
            } catch (URISyntaxException e) {
                LOG.error("websocket url({}) is error", url, e);
            }
        }
    }

    @Override
    public void close() {
        for (GatewawyWebsocketClient client : clients) {
            client.nowClose();
        }
    }
}
