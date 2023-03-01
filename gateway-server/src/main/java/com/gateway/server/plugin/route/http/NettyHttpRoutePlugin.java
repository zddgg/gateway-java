package com.gateway.server.plugin.route.http;

import com.gateway.server.constant.GatewayConstants;
import com.gateway.server.enums.PluginEnum;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.AbstractServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;

import java.net.URI;
import java.util.Optional;
import java.util.function.Consumer;

public class NettyHttpRoutePlugin extends AbstractHttpRoutePlugin<HttpClientResponse> {

    private final HttpClient httpClient;

    /**
     * Instantiates a new Netty http client plugin.
     *
     * @param httpClient the http client
     */
    public NettyHttpRoutePlugin(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    protected Mono<HttpClientResponse> doRequest(final ServerWebExchange exchange, final String httpMethod, final URI uri,
                                                 final HttpHeaders httpHeaders, final Flux<DataBuffer> body) {
        return Mono.from(httpClient.headers(headers -> httpHeaders.forEach(headers::add))
                .request(HttpMethod.valueOf(httpMethod)).uri(uri.toASCIIString())
                .send((req, nettyOutbound) -> nettyOutbound.send(body.map(dataBuffer -> ((NettyDataBuffer) dataBuffer).getNativeBuffer())))
                .responseConnection((res, connection) -> {
                    exchange.getAttributes().put(GatewayConstants.CLIENT_RESPONSE_ATTR, res);
                    exchange.getAttributes().put(GatewayConstants.CLIENT_RESPONSE_CONN_ATTR, connection);
                    ServerHttpResponse response = exchange.getResponse();
                    HttpHeaders headers = new HttpHeaders();
                    res.responseHeaders().forEach(entry -> headers.add(entry.getKey(), entry.getValue()));
                    String contentTypeValue = headers.getFirst(HttpHeaders.CONTENT_TYPE);
                    if (StringUtils.hasText(contentTypeValue)) {
                        exchange.getAttributes().put(GatewayConstants.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR, contentTypeValue);
                    }
                    HttpStatus status = HttpStatus.resolve(res.status().code());
                    if (status != null) {
                        response.setStatusCode(status);
                    } else if (response instanceof AbstractServerHttpResponse) {
                        response.setRawStatusCode(res.status().code());
                    } else {
                        throw new IllegalStateException("Unable to set status code on response: " + res.status().code() + ", " + response.getClass());
                    }
                    response.getHeaders().putAll(headers);
                    // watcher httpStatus
                    final Consumer<HttpStatus> consumer = exchange.getAttribute(GatewayConstants.WATCHER_HTTP_STATUS);
                    Optional.ofNullable(consumer).ifPresent(c -> c.accept(response.getStatusCode()));
                    return Mono.just(res);
                }));
    }

    @Override
    public int order() {
        return PluginEnum.NETTY_HTTP_CLIENT.getCode();
    }

    @Override
    public boolean skip(final ServerWebExchange exchange) {
        return false;
    }

    @Override
    public String name() {
        return "NettyHttpClientPlugin";
    }
}
