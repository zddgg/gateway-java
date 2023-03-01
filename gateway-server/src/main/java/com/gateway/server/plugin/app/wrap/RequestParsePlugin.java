package com.gateway.server.plugin.app.wrap;

import com.alibaba.fastjson2.JSON;
import com.gateway.server.chain.GatewayPluginChain;
import com.gateway.server.constant.GatewayConstants;
import com.gateway.server.enums.PluginEnum;
import com.gateway.server.plugin.AbstractGatewayPlugin;
import com.gateway.server.support.BodyInserterContext;
import com.gateway.server.support.CachedBodyOutputMessage;
import com.gateway.server.support.RequestDecorator;
import com.gateway.server.utils.ResponseUtils;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Component
public class RequestParsePlugin extends AbstractGatewayPlugin {

    private final List<HttpMessageReader<?>> messageReaders;

    public RequestParsePlugin(ServerCodecConfigurer configurer) {
        this.messageReaders = configurer.getReaders();
    }

    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, GatewayPluginChain chain) {
        ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);
        Mono<String> mono = serverRequest.bodyToMono(String.class)
                .flatMap(originalBody ->
                        Mono.just(JSON.parseObject(originalBody).getString(GatewayConstants.DATA)));
        CachedBodyOutputMessage outputMessage = ResponseUtils.newCachedBodyOutputMessage(exchange);
        return BodyInserters.fromPublisher(mono, String.class).insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() ->
                        chain.execute(exchange.mutate()
                                .request(new RequestDecorator(exchange, outputMessage)).build())))
                .onErrorResume((Function<Throwable, Mono<Void>>) throwable -> ResponseUtils.release(outputMessage, throwable));
    }

    @Override
    public boolean skip(ServerWebExchange exchange) {
        return false;
    }

    @Override
    public int order() {
        return PluginEnum.GLOBAL.getCode() + 1;
    }

    @Override
    public String name() {
        return null;
    }
}
