package com.gateway.server.utils;

import com.gateway.server.result.GatewayResult;
import com.gateway.server.result.GatewayResultWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * The type Shenyu result utils.
 */
public final class WebFluxResultUtils {

    /**
     * result utils log.
     */
    private static final Logger LOG = LoggerFactory.getLogger(WebFluxResultUtils.class);

    private WebFluxResultUtils() {
    }

    /**
     * Response result.
     *
     * @param exchange the exchange
     * @param result   the result
     * @return the result
     */
    public static Mono<Void> result(final ServerWebExchange exchange, final Object result) {
        if (Objects.isNull(result)) {
            return Mono.empty();
        }
        final GatewayResult<?> gatewayResult = GatewayResultWrap.gatewayResult();
        Object resultData = gatewayResult.format(exchange, result);
        // basic data use text/plain
        MediaType mediaType = MediaType.TEXT_PLAIN;
        if (!BeanUtils.isSimpleValueType(result.getClass())) {
            mediaType = gatewayResult.contentType(exchange, resultData);
        }
        exchange.getResponse().getHeaders().setContentType(mediaType);
        final Object responseData = gatewayResult.result(exchange, resultData);
        assert null != responseData;
        final byte[] bytes = (responseData instanceof byte[])
                ? (byte[]) responseData : responseData.toString().getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
                        .doOnNext(data -> exchange.getResponse()
                                .getHeaders().setContentLength(data.readableByteCount())));
    }
}
