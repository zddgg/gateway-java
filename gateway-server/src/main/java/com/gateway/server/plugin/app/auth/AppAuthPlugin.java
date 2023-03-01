package com.gateway.server.plugin.app.auth;

import com.gateway.server.cache.AppDataCache;
import com.gateway.server.chain.GatewayPluginChain;
import com.gateway.server.constant.GatewayConstants;
import com.gateway.server.context.GatewayContext;
import com.gateway.server.dto.AppData;
import com.gateway.server.enums.PluginEnum;
import com.gateway.server.exception.GatewayException;
import com.gateway.server.plugin.AbstractGatewayPlugin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class AppAuthPlugin extends AbstractGatewayPlugin {
    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, GatewayPluginChain chain) {
        GatewayContext context = exchange.getAttribute(GatewayConstants.CONTEXT);
        assert context != null;
        AppData appData = AppDataCache.getInstance().obtain(context.getAppKey());
        if (Objects.isNull(appData) || !StringUtils.equals(appData.getAppSecret(), context.getAppSecret())) {
            throw new GatewayException("应用密钥错误");
        }
        return chain.execute(exchange);
    }

    @Override
    public boolean skip(ServerWebExchange exchange) {
        return false;
    }

    @Override
    public int order() {
        return PluginEnum.APP_AUTH.getCode();
    }

    @Override
    public String name() {
        return "AppAuthPlugin";
    }
}
