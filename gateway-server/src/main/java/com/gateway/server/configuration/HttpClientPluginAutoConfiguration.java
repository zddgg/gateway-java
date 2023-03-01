/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gateway.server.configuration;

import com.gateway.server.plugin.GatewayPlugin;
import com.gateway.server.plugin.response.strategy.MessageWriter;
import com.gateway.server.plugin.response.strategy.NettyClientMessageWriter;
import com.gateway.server.plugin.route.http.NettyHttpRoutePlugin;
import com.gateway.server.plugin.route.http.WebClientRoutePlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.Objects;

/**
 * The type Http client plugin configuration.
 */
@Slf4j
@Configuration
public class HttpClientPluginAutoConfiguration {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create();
    }

    @Bean
    public MessageWriter nettyClientMessageWriter() {
        return new NettyClientMessageWriter();
    }

    @Configuration
    @ConditionalOnProperty(name = "mobile.gateway.httpclient.strategy", havingValue = "webClient", matchIfMissing = true)
    static class WebHttpClientConfiguration {

        @Bean
        public WebClient webClient(final ObjectProvider<HttpClient> httpClient) {
            return WebClient.builder()
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(1024 * 1024))
                            .build())
                    .clientConnector(new ReactorClientHttpConnector(Objects.requireNonNull(httpClient.getIfAvailable())))
                    .build();
        }

        @Bean
        public GatewayPlugin webHttpClientPlugin(final WebClient webClient) {
            return new WebClientRoutePlugin(webClient);
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "mobile.gateway.httpclient.strategy", havingValue = "netty")
    static class NettyHttpClientConfiguration {
        @Bean
        public GatewayPlugin nettyHttpClientPlugin(final ObjectProvider<HttpClient> httpClient) {
            return new NettyHttpRoutePlugin(httpClient.getIfAvailable());
        }
    }
}
