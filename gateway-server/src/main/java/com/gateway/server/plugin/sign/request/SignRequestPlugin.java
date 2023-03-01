package com.gateway.server.plugin.sign.request;

import com.gateway.server.chain.GatewayPluginChain;
import com.gateway.server.enums.PluginEnum;
import com.gateway.server.plugin.AbstractGatewayPlugin;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class SignRequestPlugin extends AbstractGatewayPlugin {

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
        return PluginEnum.CRYPTOR_REQUEST.getCode() - 1;
    }

    @Override
    public String name() {
        return null;
    }

}
