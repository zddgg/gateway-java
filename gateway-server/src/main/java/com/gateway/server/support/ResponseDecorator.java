package com.gateway.server.support;

import com.gateway.server.utils.ResponseUtils;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.function.Function;

/**
 * Build and modify the response class.
 */
public class ResponseDecorator extends ServerHttpResponseDecorator {

    private final ServerWebExchange exchange;

    private final Function<String, Mono<String>> convert;

    public ResponseDecorator(final ServerWebExchange exchange, final Function<String, Mono<String>> convert) {
        super(exchange.getResponse());
        this.exchange = exchange;
        this.convert = convert;
    }

    @Override
    @NonNull
    public Mono<Void> writeWith(@NonNull final Publisher<? extends DataBuffer> body) {
        ClientResponse clientResponse = ResponseUtils.buildClientResponse(this.getDelegate(), body);
        Mono<String> mono = clientResponse.bodyToMono(String.class).flatMap(convert);
        return ResponseUtils.writeWith(clientResponse, this.exchange, mono, String.class);
    }
}
