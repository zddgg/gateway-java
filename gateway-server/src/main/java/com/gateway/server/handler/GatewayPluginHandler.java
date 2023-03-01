package com.gateway.server.handler;

import com.gateway.server.chain.GatewayPluginChain;
import com.gateway.server.plugin.GatewayPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Slf4j
public class GatewayPluginHandler implements WebHandler {

    private final List<GatewayPlugin> filters;

    public GatewayPluginHandler(List<GatewayPlugin> filters) {
        this.filters = filters;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        log.info("PluginHandler");
        filters.sort(Comparator.comparingInt(GatewayPlugin::order));
        return new DefaultGatewayPluginChain(filters).execute(exchange);
    }

    private static class DefaultGatewayPluginChain implements GatewayPluginChain {

        private int index;

        private final List<GatewayPlugin> plugins;

        public DefaultGatewayPluginChain(List<GatewayPlugin> plugins) {
            this.plugins = plugins;
        }

        @Override
        public Mono<Void> execute(ServerWebExchange exchange) {
            return Mono.defer(() -> {
                if (this.index < plugins.size()) {
                    GatewayPlugin plugin = plugins.get(this.index++);
                    boolean skip = plugin.skip(exchange);
                    if (skip) {
                        return this.execute(exchange);
                    }
                    return plugin.execute(exchange, this);
                }
                return Mono.empty();
            });
        }
    }
}
