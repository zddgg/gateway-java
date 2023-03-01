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
import com.gateway.server.plugin.response.ResponsePlugin;
import com.gateway.server.plugin.response.strategy.MessageWriter;
import com.gateway.server.plugin.response.strategy.NettyClientMessageWriter;
import com.gateway.server.plugin.response.strategy.WebClientMessageWriter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConditionalOnProperty(value = {"mobile.gateway.plugins.response.enabled"}, havingValue = "true", matchIfMissing = true)
public class ResponsePluginAutoConfiguration {

    @Bean
    public GatewayPlugin responsePlugin(final ObjectProvider<List<MessageWriter>> httpWriter) {
        Map<String, MessageWriter> writerMap = new LinkedHashMap<>();
        List<MessageWriter> writerList = httpWriter.getIfAvailable(ArrayList::new);
        for (MessageWriter writer : writerList) {
            List<String> supportTypes = writer.supportTypes();
            for (String type : supportTypes) {
                writerMap.put(type, writer);
            }
        }
        return new ResponsePlugin(writerMap);
    }

    @Configuration
    @ConditionalOnProperty(name = "mobile.gateway.httpclient.strategy", havingValue = "webClient", matchIfMissing = true)
    static class WebClientMessageWriterConfiguration {

        @Bean
        public MessageWriter webClientMessageWriter() {
            return new WebClientMessageWriter();
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "mobile.gateway.httpclient.strategy", havingValue = "netty")
    static class NettyClientMessageWriterConfiguration {

        @Bean
        public MessageWriter nettyMessageWriter() {
            return new NettyClientMessageWriter();
        }
    }
}
