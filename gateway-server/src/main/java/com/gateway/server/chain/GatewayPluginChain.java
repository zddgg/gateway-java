package com.gateway.server.chain;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface GatewayPluginChain {

    Mono<Void> execute(ServerWebExchange exchange);
}
