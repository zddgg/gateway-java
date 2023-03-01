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

package com.gateway.admin.listener.websocket;

import com.alibaba.fastjson2.JSON;
import com.gateway.admin.dto.ConfigGroupEnum;
import com.gateway.admin.dto.WebsocketData;
import com.gateway.admin.dto.cache.AppData;
import com.gateway.admin.dto.cache.RouteData;
import com.gateway.admin.dto.cache.SecretData;
import com.gateway.admin.dto.cache.UpstreamData;
import com.gateway.admin.enums.DataEventTypeEnum;
import com.gateway.admin.listener.DataChangedListener;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * The type Websocket data changed listener.
 *
 * @since 2.0.0
 */
@Slf4j
public class WebsocketDataChangedListener implements DataChangedListener {

    @Override
    public void onAppChanged(final List<AppData> data, final DataEventTypeEnum eventType) {
        log.info("send cache data: {}, eventType: {}", data, eventType);
        WebsocketData<AppData> configData =
                new WebsocketData<>(ConfigGroupEnum.APP.name(), eventType.name(), data);
        WebsocketCollector.send(JSON.toJSONString(configData), eventType);
    }

    @Override
    public void onRouteChanged(final List<RouteData> data, final DataEventTypeEnum eventType) {
        log.info("send cache data: {}, eventType: {}", data, eventType);
        WebsocketData<RouteData> websocketData =
                new WebsocketData<>(ConfigGroupEnum.ROUTE.name(), eventType.name(), data);
        WebsocketCollector.send(JSON.toJSONString(websocketData), eventType);
    }

    @Override
    public void onUpstreamChanged(List<UpstreamData> data, DataEventTypeEnum eventType) {
        log.info("send cache data: {}, eventType: {}", data, eventType);
        WebsocketData<UpstreamData> websocketData =
                new WebsocketData<>(ConfigGroupEnum.UPSTREAM.name(), eventType.name(), data);
        WebsocketCollector.send(JSON.toJSONString(websocketData), eventType);
    }

    @Override
    public void onSecretChanged(List<SecretData> data, DataEventTypeEnum eventType) {
        log.info("send cache data: {}, eventType: {}", data, eventType);
        WebsocketData<SecretData> websocketData =
                new WebsocketData<>(ConfigGroupEnum.SECRET.name(), eventType.name(), data);
        WebsocketCollector.send(JSON.toJSONString(websocketData), eventType);
    }
}
