package com.gateway.server.plugin.availability;

import com.gateway.server.chain.GatewayPluginChain;
import com.gateway.server.enums.PluginEnum;
import com.gateway.server.exception.GatewayException;
import com.gateway.server.plugin.AbstractGatewayPlugin;
import com.gateway.server.plugin.availability.factory.Resilience4JRegistryFactory;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class RateLimiterPlugin extends AbstractGatewayPlugin {
    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, GatewayPluginChain chain) {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(1))
                .limitRefreshPeriod(Duration.ofSeconds(5))
                .limitForPeriod(1)
                .build();
        RateLimiter rateLimiter = Resilience4JRegistryFactory.rateLimiter("backendName", config);
        return chain.execute(exchange)
                .transformDeferred(RateLimiterOperator.of(rateLimiter))
                .doOnError(RequestNotPermitted.class, th -> log.error("请求数量已经达到上限", th))
                .onErrorMap(RequestNotPermitted.class, th -> new GatewayException("too many request, Blocked By Gateway", th));
    }

    @Override
    public boolean skip(ServerWebExchange exchange) {
        return true;
    }

    @Override
    public int order() {
        return PluginEnum.RATE_LIMITER.getCode();
    }

    @Override
    public String name() {
        return null;
    }
}
