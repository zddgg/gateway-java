package com.gateway.server.plugin.cryptor.response;

import com.gateway.server.cache.AppDataCache;
import com.gateway.server.chain.GatewayPluginChain;
import com.gateway.server.constant.GatewayConstants;
import com.gateway.server.context.GatewayContext;
import com.gateway.server.dto.AppData;
import com.gateway.server.enums.PluginEnum;
import com.gateway.server.plugin.AbstractGatewayPlugin;
import com.gateway.server.plugin.cryptor.strategy.CryptorStrategyFactory;
import com.gateway.server.support.ResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class CryptorResponsePlugin extends AbstractGatewayPlugin {

    private final CryptorStrategyFactory cryptorStrategyFactory;

    public CryptorResponsePlugin(CryptorStrategyFactory cryptorStrategyFactory) {
        this.cryptorStrategyFactory = cryptorStrategyFactory;
    }

    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, GatewayPluginChain chain) {
        GatewayContext context = exchange.getAttribute(GatewayConstants.CONTEXT);
        assert context != null;
        return chain.execute(exchange.mutate().response(
                new ResponseDecorator(exchange, originalBody ->
                        Mono.just(cryptorStrategyFactory.encrypt(originalBody, context)))).build());
    }

    @Override
    public boolean skip(ServerWebExchange exchange) {
        GatewayContext context = exchange.getAttribute(GatewayConstants.CONTEXT);
        if (Objects.nonNull(context)) {
            AppData appData = AppDataCache.getInstance().obtain(context.getAppKey());
            return !appData.getCryptorEnable();
        }
        return false;
    }

    @Override
    public int order() {
        return PluginEnum.CRYPTOR_RESPONSE.getCode();
    }

    @Override
    public String name() {
        return "CryptorResponsePlugin";
    }
}
