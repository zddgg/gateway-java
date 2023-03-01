package com.gateway.server.plugin.global;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.gateway.server.cache.AppDataCache;
import com.gateway.server.chain.GatewayPluginChain;
import com.gateway.server.constant.GatewayConstants;
import com.gateway.server.context.GatewayContext;
import com.gateway.server.dto.AppData;
import com.gateway.server.enums.PluginEnum;
import com.gateway.server.plugin.GatewayPlugin;
import com.gateway.server.support.BodyInserterContext;
import com.gateway.server.support.CachedBodyOutputMessage;
import com.gateway.server.support.RequestDecorator;
import com.gateway.server.utils.ResponseUtils;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static com.gateway.server.constant.GatewayConstants.*;

@Component
public class GlobalPlugin implements GatewayPlugin {

    private final List<HttpMessageReader<?>> messageReaders;

    public GlobalPlugin(ServerCodecConfigurer configurer) {
        this.messageReaders = configurer.getReaders();
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, GatewayPluginChain chain) {

        ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);
        Mono<String> mono = serverRequest.bodyToMono(String.class)
                .flatMap(originalBody -> {
                    buildGatewayContext(exchange, originalBody);
                    return Mono.just(originalBody);
                });

        BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(mono, String.class);
        CachedBodyOutputMessage outputMessage = ResponseUtils.newCachedBodyOutputMessage(exchange);
        return bodyInserter.insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> {
                    ServerHttpRequestDecorator decorator = new RequestDecorator(exchange, outputMessage);
                    return chain.execute(exchange.mutate().request(decorator).build());
                })).onErrorResume(throwable -> ResponseUtils.release(outputMessage, throwable));
    }

    @Override
    public boolean skip(ServerWebExchange exchange) {
        return false;
    }

    @Override
    public int order() {
        return PluginEnum.GLOBAL.getCode();
    }

    @Override
    public String name() {
        return PluginEnum.GLOBAL.getName();
    }

    private void buildGatewayContext(ServerWebExchange exchange, String body) {
        ServerHttpRequest request = exchange.getRequest();
        JSONObject jsonObject = JSON.parseObject(body);
        GatewayContext gatewayContext = new GatewayContext();
        gatewayContext.setPath(request.getURI().getPath());
        Optional.ofNullable(request.getMethod())
                .ifPresent(httpMethod -> gatewayContext.setHttpMethod(httpMethod.name()));
        gatewayContext.setAppKey(jsonObject.getString(APP_KEY));
        gatewayContext.setAppSecret(jsonObject.getString(APP_SECRET));
        gatewayContext.setRequestTimestamp(jsonObject.getString(TIMESTAMP));
        AppData appData = AppDataCache.getInstance().obtain(gatewayContext.getAppKey());
        if (appData.getCryptorEnable()) {
            gatewayContext.setRequestEncryptKey(jsonObject.getString(ENCRYPT_KEY));
            gatewayContext.setRequestSign(jsonObject.getString(SIGN));
            gatewayContext.setCryptorType("Sm2Sm4CryptorStrategy");
        }
        exchange.getAttributes().put(GatewayConstants.CONTEXT, gatewayContext);
    }
}
