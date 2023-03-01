package com.gateway.server.plugin.sign.response;

import com.gateway.server.chain.GatewayPluginChain;
import com.gateway.server.enums.PluginEnum;
import com.gateway.server.plugin.AbstractGatewayPlugin;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class SignResponsePlugin extends AbstractGatewayPlugin {
    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, GatewayPluginChain chain) {
        return null;
    }

    @Override
    public boolean skip(ServerWebExchange exchange) {
        return false;
    }

    @Override
    public int order() {
        return PluginEnum.CRYPTOR_RESPONSE.getCode() + 1;
    }

    @Override
    public String name() {
        return null;
    }
}
