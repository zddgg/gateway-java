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

import com.gateway.server.sync.websocket.WebsocketSyncDataService;
import com.gateway.server.sync.websocket.config.WebsocketConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Websocket sync data configuration for spring boot.
 */
@Configuration
@ConditionalOnClass(WebsocketSyncDataService.class)
@ConditionalOnProperty(prefix = "mobile.gateway.sync.websocket", name = "urls")
public class WebsocketSyncDataConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketSyncDataConfiguration.class);

//    @Bean
//    public SyncDataService websocketSyncDataService(final ObjectProvider<WebsocketConfig> websocketConfig,
//                                                    final ObjectProvider<List<AppDataSubscriber>> appDataSubscribes,
//                                                    final ObjectProvider<List<RouteDataSubscriber>> routeDataSubscribes) {
//        LOGGER.info("you use websocket sync shenyu data.......");
//        return new WebsocketSyncDataService(websocketConfig.getIfAvailable(WebsocketConfig::new),
//                appDataSubscribes.getIfAvailable(Collections::emptyList),
//                routeDataSubscribes.getIfAvailable(Collections::emptyList)
//        );
//    }

    /**
     * Config websocket config.
     *
     * @return the websocket config
     */
    @Bean
    @ConfigurationProperties(prefix = "mobile.gateway.sync.websocket")
    public WebsocketConfig websocketConfig() {
        return new WebsocketConfig();
    }

}
