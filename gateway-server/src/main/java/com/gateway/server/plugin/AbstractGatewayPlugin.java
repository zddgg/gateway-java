package com.gateway.server.plugin;

import com.gateway.server.chain.GatewayPluginChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public abstract class AbstractGatewayPlugin implements GatewayPlugin {

    protected abstract Mono<Void> doExecute(ServerWebExchange exchange, GatewayPluginChain chain);

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, GatewayPluginChain chain) {
        return doExecute(exchange, chain);
    }
}
