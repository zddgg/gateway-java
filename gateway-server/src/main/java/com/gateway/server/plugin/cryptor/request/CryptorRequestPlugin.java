package com.gateway.server.plugin.cryptor.request;

import com.gateway.server.cache.AppDataCache;
import com.gateway.server.chain.GatewayPluginChain;
import com.gateway.server.constant.GatewayConstants;
import com.gateway.server.context.GatewayContext;
import com.gateway.server.dto.AppData;
import com.gateway.server.enums.PluginEnum;
import com.gateway.server.plugin.AbstractGatewayPlugin;
import com.gateway.server.plugin.cryptor.strategy.CryptorStrategyFactory;
import com.gateway.server.support.BodyInserterContext;
import com.gateway.server.support.CachedBodyOutputMessage;
import com.gateway.server.support.RequestDecorator;
import com.gateway.server.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Component
public class CryptorRequestPlugin extends AbstractGatewayPlugin {

    private final List<HttpMessageReader<?>> messageReaders;

    private final CryptorStrategyFactory cryptorStrategyFactory;

    public CryptorRequestPlugin(ServerCodecConfigurer configurer,
                                CryptorStrategyFactory cryptorStrategyFactory) {
        this.messageReaders = configurer.getReaders();
        this.cryptorStrategyFactory = cryptorStrategyFactory;
    }

    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, GatewayPluginChain chain) {
        GatewayContext context = exchange.getAttribute(GatewayConstants.CONTEXT);
        assert context != null;
        ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);
        Mono<String> mono = serverRequest.bodyToMono(String.class)
                .flatMap(originalBody -> strategyMatch(originalBody, context));

        BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(mono, String.class);
        CachedBodyOutputMessage outputMessage = ResponseUtils.newCachedBodyOutputMessage(exchange);
        return bodyInserter.insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> {
                    ServerHttpRequestDecorator decorator = new RequestDecorator(exchange, outputMessage);
                    return chain.execute(exchange.mutate().request(decorator).build());
                })).onErrorResume((Function<Throwable, Mono<Void>>) throwable -> ResponseUtils.release(outputMessage, throwable));
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
        return PluginEnum.CRYPTOR_REQUEST.getCode();
    }

    @Override
    public String name() {
        return PluginEnum.CRYPTOR_REQUEST.getName();
    }

    private Mono<String> strategyMatch(final String originalBody, GatewayContext context) {
        return Mono.just(cryptorStrategyFactory.decrypt(originalBody, context));
    }
}
