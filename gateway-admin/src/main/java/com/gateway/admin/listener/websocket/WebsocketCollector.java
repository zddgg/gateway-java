package com.gateway.admin.listener.websocket;

import com.gateway.admin.enums.DataEventTypeEnum;
import com.gateway.admin.service.NodeInfoService;
import com.gateway.admin.service.SyncDataService;
import com.gateway.admin.spring.SpringBeanUtils;
import com.gateway.admin.utils.ThreadLocalUtils;
import com.gateway.admin.utils.URIUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The type Websocket data changed listener.
 *
 * @since 2.0.0
 */
@ServerEndpoint(value = "/websocket", configurator = WebsocketConfigurator.class)
public class WebsocketCollector {

    private static final Logger LOG = LoggerFactory.getLogger(WebsocketCollector.class);

    private static final Set<Session> SESSION_SET = new CopyOnWriteArraySet<>();

    private static final String SESSION_KEY = "sessionKey";

    private final NodeInfoService nodeInfoService;

    public WebsocketCollector() {
        nodeInfoService = SpringBeanUtils.getInstance().getBean(NodeInfoService.class);
    }

    /**
     * On open.
     *
     * @param session the session
     */
    @OnOpen
    public void onOpen(final Session session) {
        LOG.info("websocket on client[{}] open successful,maxTextMessageBufferSize:{}",
                getClientIp(session), session.getMaxTextMessageBufferSize());
        SESSION_SET.add(session);
        String host = URIUtil.getParameter(session.getRequestURI(), "host");
        String port = URIUtil.getParameter(session.getRequestURI(), "port");
        nodeInfoService.registerIfNotExist(host, port);
    }

    private static String getClientIp(final Session session) {
        Map<String, Object> userProperties = session.getUserProperties();
        if (CollectionUtils.isEmpty(userProperties)) {
            return StringUtils.EMPTY;
        }

        return Optional.ofNullable(userProperties.get(WebsocketListener.CLIENT_IP_NAME))
                .map(Object::toString)
                .orElse(StringUtils.EMPTY);
    }

    /**
     * On message.
     *
     * @param message the message
     * @param session the session
     */
    @OnMessage
    public void onMessage(final String message, final Session session) {
        if (!Objects.equals(message, DataEventTypeEnum.MYSELF.name())) {
            return;
        }

        try {
            ThreadLocalUtils.put(SESSION_KEY, session);
            SpringBeanUtils.getInstance().getBean(SyncDataService.class).syncAll(DataEventTypeEnum.MYSELF);
        } finally {
            ThreadLocalUtils.clear();
        }

    }

    /**
     * On close.
     *
     * @param session the session
     */
    @OnClose
    public void onClose(final Session session) {
        clearSession(session);
        LOG.warn("websocket close on client[{}]", getClientIp(session));
    }

    /**
     * On error.
     *
     * @param session the session
     * @param error   the error
     */
    @OnError
    public void onError(final Session session, final Throwable error) {
        clearSession(session);
        LOG.error("websocket collection on client[{}] error: ", getClientIp(session), error);
    }

    /**
     * Send.
     *
     * @param message the message
     * @param type    the type
     */
    public static void send(final String message, final DataEventTypeEnum type) {
        if (StringUtils.isBlank(message)) {
            return;
        }

        if (DataEventTypeEnum.MYSELF == type) {
            Session session = (Session) ThreadLocalUtils.get(SESSION_KEY);
            if (Objects.nonNull(session)) {
                sendMessageBySession(session, message);
            }
        } else {
            SESSION_SET.forEach(session -> sendMessageBySession(session, message));
        }

    }

    private static synchronized void sendMessageBySession(final Session session, final String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            LOG.error("websocket send result is exception: ", e);
        }
    }

    private void clearSession(final Session session) {
        SESSION_SET.remove(session);
        ThreadLocalUtils.clear();
    }
}
