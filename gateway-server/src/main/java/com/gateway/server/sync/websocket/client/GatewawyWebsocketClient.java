package com.gateway.server.sync.websocket.client;

import com.alibaba.fastjson2.JSON;
import com.gateway.server.dto.WebsocketData;
import com.gateway.server.enums.ConfigGroupEnum;
import com.gateway.server.enums.DataEventTypeEnum;
import com.gateway.server.sync.AppDataSubscriber;
import com.gateway.server.sync.RouteDataSubscriber;
import com.gateway.server.sync.SecretDataSubscriber;
import com.gateway.server.sync.UpstreamDataSubscriber;
import com.gateway.server.sync.websocket.handler.*;
import com.gateway.server.timer.AbstractRoundTask;
import com.gateway.server.timer.Timer;
import com.gateway.server.timer.TimerTask;
import com.gateway.server.timer.WheelTimerFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class GatewawyWebsocketClient extends WebSocketClient {

    private static final Logger LOG = LoggerFactory.getLogger(GatewawyWebsocketClient.class);

    private static final EnumMap<ConfigGroupEnum, DataHandler> HANDLER_ENUM_MAP = new EnumMap<>(ConfigGroupEnum.class);

    private volatile boolean alreadySync = Boolean.FALSE;

    private final Timer timer;

    private TimerTask timerTask;

    public GatewawyWebsocketClient(final URI serverUri,
                                   final List<AppDataSubscriber> appDataSubscribers,
                                   final List<RouteDataSubscriber> routeDataSubscribers,
                                   final List<UpstreamDataSubscriber> upstreamDataSubscribers,
                                   final List<SecretDataSubscriber> secretDataSubscribers) {
        super(serverUri);
        this.timer = WheelTimerFactory.getSharedTimer();
        this.connection();
        HANDLER_ENUM_MAP.put(ConfigGroupEnum.APP, new AppDataHandler(appDataSubscribers));
        HANDLER_ENUM_MAP.put(ConfigGroupEnum.ROUTE, new RouteDataHandler(routeDataSubscribers));
        HANDLER_ENUM_MAP.put(ConfigGroupEnum.UPSTREAM, new UpstreamDataHandler(upstreamDataSubscribers));
        HANDLER_ENUM_MAP.put(ConfigGroupEnum.SECRET, new SecretDataHandler(secretDataSubscribers));
    }

    public GatewawyWebsocketClient(final URI serverUri, final Map<String, String> headers,
                                   final List<AppDataSubscriber> appDataSubscribers,
                                   final List<RouteDataSubscriber> routeDataSubscribers,
                                   final List<UpstreamDataSubscriber> upstreamDataSubscribers,
                                   final List<SecretDataSubscriber> secretDataSubscribers) {
        super(serverUri, headers);
        this.timer = WheelTimerFactory.getSharedTimer();
        this.connection();
        HANDLER_ENUM_MAP.put(ConfigGroupEnum.APP, new AppDataHandler(appDataSubscribers));
        HANDLER_ENUM_MAP.put(ConfigGroupEnum.ROUTE, new RouteDataHandler(routeDataSubscribers));
        HANDLER_ENUM_MAP.put(ConfigGroupEnum.UPSTREAM, new UpstreamDataHandler(upstreamDataSubscribers));
        HANDLER_ENUM_MAP.put(ConfigGroupEnum.SECRET, new SecretDataHandler(secretDataSubscribers));
    }

    private void connection() {
        this.connectBlocking();
        this.timer.add(timerTask = new AbstractRoundTask(null, TimeUnit.SECONDS.toMillis(10)) {
            @Override
            public void doRun(final String key, final TimerTask timerTask) {
                healthCheck();
            }
        });
    }

    @Override
    public boolean connectBlocking() {
        boolean success = false;
        try {
            success = super.connectBlocking();
        } catch (Exception exception) {
            LOG.error("websocket connection server[{}] is error.....[{}]", this.getURI().toString(), exception.getMessage());
        }
        if (success) {
            LOG.info("websocket connection server[{}] is successful.....", this.getURI().toString());
        } else {
            LOG.warn("websocket connection server[{}] is error.....", this.getURI().toString());
        }
        return success;
    }

    @Override
    public void onOpen(final ServerHandshake serverHandshake) {
        if (!alreadySync) {
            send(DataEventTypeEnum.MYSELF.name());
            alreadySync = true;
        }
    }

    @Override
    public void onMessage(final String result) {
        handleResult(result);
    }

    @Override
    public void onClose(final int i, final String s, final boolean b) {
        this.close();
    }

    @Override
    public void onError(final Exception e) {
        LOG.error("websocket server[{}] is error.....", getURI(), e);
    }

    @Override
    public void close() {
        alreadySync = false;
        if (this.isOpen()) {
            super.close();
        }
    }

    /**
     * Now close.
     * now close. will cancel the task execution.
     */
    public void nowClose() {
        this.close();
        timerTask.cancel();
    }

    private void healthCheck() {
        try {
            if (!this.isOpen()) {
                this.reconnectBlocking();
            } else {
                this.sendPing();
                LOG.debug("websocket send to [{}] ping message successful", this.getURI());
            }
        } catch (Exception e) {
            LOG.error("websocket connect is error :{}", e.getMessage());
        }
    }

    private void handleResult(final String result) {
        LOG.info("handleResult({})", result);
        WebsocketData<?> websocketData = JSON.parseObject(result, WebsocketData.class);
        ConfigGroupEnum groupEnum = ConfigGroupEnum.acquireByName(websocketData.getGroupType());
        String eventType = websocketData.getEventType();
        String json = JSON.toJSONString(websocketData.getData());
        HANDLER_ENUM_MAP.get(groupEnum).handle(json, eventType);
    }
}
