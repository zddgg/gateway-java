package com.gateway.server.plugin;

import com.gateway.server.chain.GatewayPluginChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface GatewayPlugin {
    Mono<Void> execute(ServerWebExchange exchange, GatewayPluginChain chain);

    boolean skip(ServerWebExchange exchange);

    int order();

    String name();
}
