package com.gateway.server.plugin.response.strategy;

import com.gateway.server.constant.GatewayConstants;
import com.gateway.server.enums.RpcTypeEnum;
import com.google.common.collect.Lists;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class NettyClientMessageWriter extends AbstractMessageWriter {

    /**
     * stream media type: from APPLICATION_STREAM_JSON upgrade to APPLICATION_STREAM_JSON_VALUE.
     * Both of the above have expired.
     * latest version: {@linkplain MediaType#APPLICATION_NDJSON}
     */
    private final List<MediaType> streamingMediaTypes = Arrays.asList(MediaType.TEXT_EVENT_STREAM, MediaType.APPLICATION_NDJSON);

    @Override
    public Mono<Void> doWrite(final ServerWebExchange exchange) {
        Connection connection = exchange.getAttribute(GatewayConstants.CLIENT_RESPONSE_CONN_ATTR);
        if (Objects.isNull(connection)) {
            return Mono.empty();
        }
        ServerHttpResponse response = exchange.getResponse();
        NettyDataBufferFactory factory = (NettyDataBufferFactory) response.bufferFactory();
        final Flux<NettyDataBuffer> body = connection
                .inbound()
                .receive()
                .retain()
                .map(factory::wrap);
        MediaType contentType = response.getHeaders().getContentType();

        Mono<Void> responseMono = isStreamingMediaType(contentType)
                ? response.writeAndFlushWith(body.map(Flux::just))
                : response.writeWith(body);
        exchange.getAttributes().put(GatewayConstants.RESPONSE_MONO, responseMono);
        // watcher httpStatus
        final Consumer<HttpStatus> consumer = exchange.getAttribute(GatewayConstants.WATCHER_HTTP_STATUS);
        Optional.ofNullable(consumer).ifPresent(c -> c.accept(response.getStatusCode()));
        return responseMono;
    }

    @Override
    public List<String> supportTypes() {
        return Lists.newArrayList(RpcTypeEnum.HTTP.getName(), RpcTypeEnum.SPRING_CLOUD.getName());
    }

    protected void cleanup(final ServerWebExchange exchange) {
        Connection connection = exchange.getAttribute(GatewayConstants.CLIENT_RESPONSE_CONN_ATTR);
        if (Objects.nonNull(connection)) {
            connection.dispose();
        }
    }

    private boolean isStreamingMediaType(@Nullable final MediaType contentType) {
        return Objects.nonNull(contentType) && this.streamingMediaTypes.stream().anyMatch(contentType::isCompatibleWith);
    }
}
