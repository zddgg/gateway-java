package com.gateway.server.result;

import com.alibaba.fastjson2.JSON;
import com.gateway.server.constant.GatewayConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;

public interface GatewayResult<T> {

    default Object result(ServerWebExchange exchange, Object formatted) {
        return formatted;
    }

    default Object format(ServerWebExchange exchange, Object origin) {
        // basic data or upstream data
        if (BeanUtils.isSimpleValueType(origin.getClass()) || (origin instanceof byte[])) {
            return origin;
        }
        // error result or rpc origin result.
        return JSON.toJSONString(origin);
    }

    default MediaType contentType(ServerWebExchange exchange, Object formatted) {
        final ClientResponse clientResponse = exchange.getAttribute(GatewayConstants.CLIENT_RESPONSE_ATTR);
        if (Objects.nonNull(clientResponse) && clientResponse.headers().contentType().isPresent()) {
            return clientResponse.headers().contentType().get();
        }
        return MediaType.APPLICATION_JSON;
    }

    /**
     * Error t.
     *
     * @param exchange the exchange
     * @param code     the code
     * @param message  the message
     * @param object   the object
     * @return the t
     */
    default T error(ServerWebExchange exchange, int code, String message, Object object) {
        return error(code, message, object);
    }

    /**
     * Error t.
     *
     * @param code    the code
     * @param message the message
     * @param object  the object
     * @return the t
     */
    default T error(int code, String message, Object object) {
        return null;
    }
}
