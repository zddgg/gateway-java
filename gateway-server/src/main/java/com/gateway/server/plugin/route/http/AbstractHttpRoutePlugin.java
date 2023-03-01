package com.gateway.server.plugin.route.http;

import com.gateway.server.cache.RouteDataCache;
import com.gateway.server.cache.UpstreamDataCache;
import com.gateway.server.chain.GatewayPluginChain;
import com.gateway.server.constant.GatewayConstants;
import com.gateway.server.context.GatewayContext;
import com.gateway.server.dto.RouteData;
import com.gateway.server.dto.UpstreamData;
import com.gateway.server.plugin.GatewayPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class AbstractHttpRoutePlugin<R> implements GatewayPlugin {

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, GatewayPluginChain chain) {
        log.info("execute plugin {}", name());
        GatewayContext context = exchange.getAttribute(GatewayConstants.CONTEXT);
        assert context != null;
        RouteData routeData = RouteDataCache.getInstance().obtain(context.getPath());
        UpstreamData upstreamData = UpstreamDataCache.getInstance().obtain(routeData.getUpstreamId());
        final URI uri = URI.create(upstreamData.getHttpAddress());
        final long timeout = 3000L;
        final Duration duration = Duration.ofMillis(timeout);

        log.info("The request urlPath is {}", uri.toASCIIString());
        final HttpHeaders httpHeaders = buildHttpHeaders(exchange);
        return doRequest(exchange, exchange.getRequest().getMethodValue(), uri, httpHeaders, exchange.getRequest().getBody())
                .timeout(duration, Mono.error(new TimeoutException("Response took longer than timeout: " + duration)))
                .doOnError(e -> log.error(e.getMessage(), e))
                .onErrorMap(TimeoutException.class, th -> new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT, th.getMessage(), th))
                .flatMap(o -> chain.execute(exchange));
    }

    protected abstract Mono<R> doRequest(ServerWebExchange exchange, String httpMethod,
                                         URI uri, HttpHeaders httpHeaders, Flux<DataBuffer> body);

    private HttpHeaders buildHttpHeaders(final ServerWebExchange exchange) {
        final HttpHeaders headers = new HttpHeaders();
        headers.addAll(exchange.getRequest().getHeaders());
        // remove gzip
        List<String> acceptEncoding = headers.get(HttpHeaders.ACCEPT_ENCODING);
        if (!CollectionUtils.isEmpty(acceptEncoding)) {
            acceptEncoding = Stream.of(String.join(",", acceptEncoding).split(",")).collect(Collectors.toList());
            acceptEncoding.remove(GatewayConstants.HTTP_ACCEPT_ENCODING_GZIP);
            headers.set(HttpHeaders.ACCEPT_ENCODING, String.join(",", acceptEncoding));
        }
        return headers;
    }
}
