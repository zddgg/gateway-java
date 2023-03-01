package com.gateway.server.plugin.modify.response;

import com.gateway.server.chain.GatewayPluginChain;
import com.gateway.server.enums.PluginEnum;
import com.gateway.server.plugin.AbstractGatewayPlugin;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ModifyResponsePlugin extends AbstractGatewayPlugin {
    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, GatewayPluginChain chain) {
        return chain.execute(exchange);
    }

    @Override
    public boolean skip(ServerWebExchange exchange) {
        return false;
    }

    @Override
    public int order() {
        return PluginEnum.MODIFY_RESPONSE.getCode();
    }

    @Override
    public String name() {
        return null;
    }
}
