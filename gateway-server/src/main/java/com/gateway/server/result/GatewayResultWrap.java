package com.gateway.server.result;

import com.gateway.server.utils.SpringBeanUtils;
import org.springframework.web.server.ServerWebExchange;

public class GatewayResultWrap<T> {

    private GatewayResultWrap() {
    }

    /**
     * Success object.
     *
     * @param exchange the exchange
     * @param object   the object
     * @return the success object
     */
    public static Object success(final ServerWebExchange exchange, final Object object) {
        return gatewayResult().result(exchange, object);
    }

    /**
     * Error object.
     *
     * @param exchange      the exchange
     * @param gatewayResult the shenyuResult
     * @param object        the object
     * @return the object
     */
    public static Object error(final ServerWebExchange exchange, final GatewayResultEnum gatewayResult, final Object object) {
        return gatewayResult().error(exchange, gatewayResult.getCode(), gatewayResult.getMsg(), object);
    }

    /**
     * Error object.
     *
     * @param gatewayResult the shenyuResult
     * @param object        the object
     * @return the object
     */
    public static Object error(final GatewayResultEnum gatewayResult, final Object object) {
        return gatewayResult().error(gatewayResult.getCode(), gatewayResult.getMsg(), object);
    }

    /**
     * Error object.
     *
     * @param exchange      the exchange
     * @param gatewayResult the shenyuResult
     * @return the object
     */
    public static Object error(final ServerWebExchange exchange, final GatewayResultEnum gatewayResult) {
        return gatewayResult().error(exchange, gatewayResult.getCode(), gatewayResult.getMsg(), null);
    }

    /**
     * Error object.
     *
     * @param exchange the exchange
     * @param code     the code
     * @param message  the message
     * @param object   the object
     * @return the object
     */
    public static Object error(final ServerWebExchange exchange, final int code, final String message, final Object object) {
        return gatewayResult().error(exchange, code, message, object);
    }

    /**
     * shenyu result bean.
     *
     * @return the shenyu result bean.
     */
    public static GatewayResult<?> gatewayResult() {
        return SpringBeanUtils.getInstance().getBean(GatewayResult.class);
    }
}
