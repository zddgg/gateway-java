package com.gateway.server.configuration;

import com.gateway.server.handler.GatewayPluginHandler;
import com.gateway.server.handler.GatewayPluginHandlerMapping;
import com.gateway.server.plugin.GatewayPlugin;
import com.gateway.server.result.DefaultGatewayResult;
import com.gateway.server.result.GatewayResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;

import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
@EnableConfigurationProperties
@AutoConfigureBefore({HttpHandlerAutoConfiguration.class,
        WebFluxAutoConfiguration.class})
@ConditionalOnClass(DispatcherHandler.class)
public class GatewayAutoConfiguration {

    @Bean
    public GatewayPluginHandler gatewayHandler(final ObjectProvider<List<GatewayPlugin>> plugins) {
        List<GatewayPlugin> pluginList = plugins.getIfAvailable(Collections::emptyList);
        pluginList.forEach(gatewayPlugin -> log.info("load plugin:[{}]", gatewayPlugin.getClass().getName()));
        return new GatewayPluginHandler(pluginList);
    }

    @Bean
    public GatewayPluginHandlerMapping routePredicateHandlerMapping(GatewayPluginHandler webHandler) {
        return new GatewayPluginHandlerMapping(webHandler);
    }

    @Bean
    @ConditionalOnMissingBean(value = GatewayResult.class, search = SearchStrategy.ALL)
    public GatewayResult<?> gatewayResult() {
        return new DefaultGatewayResult();
    }
}
