package com.gateway.server.plugin.response.strategy;

import com.gateway.server.chain.GatewayPluginChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public abstract class AbstractMessageWriter implements MessageWriter {

    public Mono<Void> write(final ServerWebExchange exchange, final GatewayPluginChain chain) {
        return chain.execute(exchange)
                .doOnError(throwable -> cleanup(exchange))
                .then(Mono.defer(() -> doWrite(exchange)))
                .doOnCancel(() -> cleanup(exchange))
                ;
    }

    protected abstract void cleanup(final ServerWebExchange exchange);

    protected abstract Mono<Void> doWrite(ServerWebExchange exchange);

}
