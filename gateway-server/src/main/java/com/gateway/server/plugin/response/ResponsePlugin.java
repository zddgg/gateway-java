package com.gateway.server.plugin.response;

import com.gateway.server.chain.GatewayPluginChain;
import com.gateway.server.enums.PluginEnum;
import com.gateway.server.plugin.GatewayPlugin;
import com.gateway.server.plugin.response.strategy.MessageWriter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

public class ResponsePlugin implements GatewayPlugin {

    private final Map<String, MessageWriter> writerMap;

    public ResponsePlugin(Map<String, MessageWriter> writerMap) {
        this.writerMap = writerMap;
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, GatewayPluginChain chain) {
        return writerMap.get("http").write(exchange, chain);
    }

    @Override
    public boolean skip(ServerWebExchange exchange) {
        return false;
    }

    @Override
    public int order() {
        return PluginEnum.RESPONSE.getCode();
    }

    @Override
    public String name() {
        return "ResponsePlugin";
    }
}
