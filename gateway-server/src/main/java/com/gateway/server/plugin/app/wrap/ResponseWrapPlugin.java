package com.gateway.server.plugin.app.wrap;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.gateway.server.cache.AppDataCache;
import com.gateway.server.chain.GatewayPluginChain;
import com.gateway.server.constant.GatewayConstants;
import com.gateway.server.context.GatewayContext;
import com.gateway.server.dto.AppData;
import com.gateway.server.enums.PluginEnum;
import com.gateway.server.plugin.AbstractGatewayPlugin;
import com.gateway.server.support.ResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ResponseWrapPlugin extends AbstractGatewayPlugin {

    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, GatewayPluginChain chain) {
        GatewayContext context = exchange.getAttribute(GatewayConstants.CONTEXT);
        assert context != null;
        return chain.execute(exchange.mutate()
                .response(new ResponseDecorator(exchange, originalBody ->
                        Mono.just(buildResponse(originalBody, context)))).build());
    }

    @Override
    public boolean skip(ServerWebExchange exchange) {
        return false;
    }

    @Override
    public int order() {
        return PluginEnum.GLOBAL.getCode() + 1;
    }

    @Override
    public String name() {
        return null;
    }

    private String buildResponse(String originalBody, GatewayContext context) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(GatewayConstants.APP_KEY, context.getAppKey());
        jsonObject.put(GatewayConstants.APP_SECRET, context.getAppKey());
        jsonObject.put(GatewayConstants.TIMESTAMP, context.getResponseTimestamp());
        jsonObject.put(GatewayConstants.DATA, JSON.isValidObject(originalBody)
                ? JSON.parseObject(originalBody)
                : originalBody);
        AppData appData = AppDataCache.getInstance().obtain(context.getAppKey());
        if (appData.getCryptorEnable()) {
            jsonObject.put(GatewayConstants.ENCRYPT_KEY, context.getResponseEncryptKey());
            jsonObject.put(GatewayConstants.SIGN, context.getResponseSign());
        }
        return jsonObject.toJSONString();
    }
}
