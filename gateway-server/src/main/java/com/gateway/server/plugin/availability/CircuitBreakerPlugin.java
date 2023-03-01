package com.gateway.server.plugin.availability;

import com.gateway.server.chain.GatewayPluginChain;
import com.gateway.server.enums.PluginEnum;
import com.gateway.server.exception.GatewayException;
import com.gateway.server.plugin.AbstractGatewayPlugin;
import com.gateway.server.plugin.availability.factory.Resilience4JRegistryFactory;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class CircuitBreakerPlugin extends AbstractGatewayPlugin {
    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, GatewayPluginChain chain) {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .failureRateThreshold(10)
                .minimumNumberOfCalls(10)
                .build();
        CircuitBreaker circuitBreaker = Resilience4JRegistryFactory.circuitBreaker("backendName", config);
        return chain.execute(exchange)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .onErrorMap(CallNotPermittedException.class, th -> new GatewayException("Blocked By Gateway", th));
    }

    @Override
    public boolean skip(ServerWebExchange exchange) {
        return false;
    }

    @Override
    public int order() {
        return PluginEnum.NETTY_HTTP_CLIENT.getCode() - 1;
    }

    @Override
    public String name() {
        return null;
    }
}
