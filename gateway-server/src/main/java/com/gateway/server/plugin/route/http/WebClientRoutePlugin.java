package com.gateway.server.plugin.route.http;

import com.gateway.server.constant.GatewayConstants;
import com.gateway.server.enums.PluginEnum;
import com.gateway.server.enums.ResultEnum;
import com.gateway.server.exception.GatewayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@Slf4j
public class WebClientRoutePlugin extends AbstractHttpRoutePlugin<ClientResponse> {


    private final WebClient webClient;

    public WebClientRoutePlugin(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    protected Mono<ClientResponse> doRequest(ServerWebExchange exchange, String httpMethod, URI uri, HttpHeaders httpHeaders, Flux<DataBuffer> body) {
        return webClient.method(HttpMethod.valueOf(httpMethod)).uri(uri)
                .headers(headers -> headers.addAll(httpHeaders))
                .body(BodyInserters.fromDataBuffers(body))
                .exchangeToMono(response -> response.bodyToMono(byte[].class)
                        .flatMap(bytes -> Mono.fromCallable(() -> Optional.ofNullable(bytes))).defaultIfEmpty(Optional.empty())
                        .flatMap(option -> {
                            final ClientResponse.Builder builder = ClientResponse.create(response.statusCode())
                                    .headers(headers -> headers.addAll(response.headers().asHttpHeaders()))
                                    .cookies(cookies -> cookies.addAll(response.cookies()));
                            if (option.isPresent()) {
                                final DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
                                return Mono.just(builder.body(Flux.just(dataBufferFactory.wrap(option.get()))).build());
                            }
                            return Mono.just(builder.build());
                        }))
                .doOnSuccess(res -> {
                    if (res.statusCode().is2xxSuccessful()) {
                        exchange.getAttributes().put(GatewayConstants.CLIENT_RESPONSE_RESULT_TYPE, ResultEnum.SUCCESS.getName());
                    } else {
                        exchange.getAttributes().put(GatewayConstants.CLIENT_RESPONSE_RESULT_TYPE, ResultEnum.ERROR.getName());
                        throw new GatewayException("请求上游服务异常");
                    }
                    exchange.getResponse().setStatusCode(res.statusCode());
                    exchange.getAttributes().put(GatewayConstants.CLIENT_RESPONSE_ATTR, res);
                });
    }

    @Override
    public boolean skip(ServerWebExchange exchange) {
        return false;
    }

    @Override
    public int order() {
        return PluginEnum.WEB_CLIENT.getCode();
    }

    @Override
    public String name() {
        return "WebClientPlugin";
    }
}
