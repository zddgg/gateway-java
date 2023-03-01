package com.gateway.server.utils;

import com.gateway.server.exception.GatewayException;
import com.gateway.server.support.BodyInserterContext;
import com.gateway.server.support.CachedBodyOutputMessage;
import com.gateway.server.support.RequestDecorator;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class ServerWebExchangeUtils {

    public static Mono<ServerWebExchange> rewriteRequestBody(final ServerWebExchange exchange,
                                                             final List<HttpMessageReader<?>> readers,
                                                             final Function<String, Mono<String>> convert) {

        ServerRequest serverRequest = ServerRequest.create(exchange, readers);
        CachedBodyOutputMessage outputMessage = ResponseUtils.newCachedBodyOutputMessage(exchange);

        return serverRequest.bodyToMono(String.class)
                .switchIfEmpty(Mono.defer(() -> Mono.just("")))
                .flatMap(convert)
                .flatMap(body -> {
                    BodyInserter<String, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromValue(body);
                    return bodyInserter.insert(outputMessage, new BodyInserterContext());
                })
                .then(Mono.just(exchange.mutate()
                        .request(new RequestDecorator(exchange, outputMessage)).build()))
                .onErrorResume(throwable -> ResponseUtils.release(outputMessage, throwable));

    }

    public static Mono<ServerWebExchange> rewriteResponseBody(final ServerWebExchange exchange) {
        return Mono.just(exchange.mutate().response(new ModifyResponseDecorator(exchange)).build());
    }

    static class ModifyResponseDecorator extends ServerHttpResponseDecorator {

        private final ServerWebExchange exchange;

        ModifyResponseDecorator(final ServerWebExchange exchange) {
            super(exchange.getResponse());
            this.exchange = exchange;
        }

        @Override
        @NonNull
        public Mono<Void> writeWith(@NonNull final Publisher<? extends DataBuffer> body) {
            ClientResponse clientResponse = this.buildModifiedResponse(body);
            Mono<byte[]> modifiedBody = clientResponse.bodyToMono(byte[].class)
                    .flatMap(originalBody -> Mono.just(modifyBody(originalBody)));
            return ResponseUtils.writeWith(clientResponse, this.exchange, modifiedBody, byte[].class);
        }

        private ClientResponse buildModifiedResponse(final Publisher<? extends DataBuffer> body) {
            HttpHeaders httpHeaders = new HttpHeaders();
            // add origin headers
            httpHeaders.addAll(this.getHeaders());

            // reset http status
            ClientResponse clientResponse = ResponseUtils.buildClientResponse(this.getDelegate(), body);
            HttpStatus statusCode = clientResponse.statusCode();

            // reset http headers
            this.getDelegate().getHeaders().clear();
            this.getDelegate().getHeaders().putAll(httpHeaders);
            int rowStatusCode = clientResponse.rawStatusCode();

            return ClientResponse.create(statusCode)
                    .rawStatusCode(rowStatusCode)
                    .headers(headers -> headers.addAll(httpHeaders))
                    .cookies(cookies -> cookies.addAll(this.getCookies()))
                    .body(Flux.from(body)).build();
        }

        private byte[] modifyBody(final byte[] responseBody) {
            try {
                String bodyStr = modifyBody(new String(responseBody, StandardCharsets.UTF_8));
                log.info("the body string {}", bodyStr);
                return bodyStr.getBytes(StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.error("modify response error", e);
                throw new GatewayException(String.format("response modify failure. %s", e.getLocalizedMessage()));
            }
        }

        private String modifyBody(final String jsonValue) {
            return "modify response xxxxxxxxx";
        }
    }
}
