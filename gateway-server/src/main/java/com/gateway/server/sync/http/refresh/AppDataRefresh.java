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

package com.gateway.server.sync.http.refresh;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.gateway.server.dto.AppData;
import com.gateway.server.dto.ConfigData;
import com.gateway.server.enums.ConfigGroupEnum;
import com.gateway.server.sync.AppDataSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * The type app auth data refresh.
 */
public class AppDataRefresh extends AbstractDataRefresh<AppData> {

    /**
     * logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AppDataRefresh.class);

    private final List<AppDataSubscriber> appDataSubscribers;

    public AppDataRefresh(final List<AppDataSubscriber> appDataSubscribers) {
        this.appDataSubscribers = appDataSubscribers;
    }

    @Override
    protected JSONObject convert(final JSONObject data) {
        return data.getJSONObject(ConfigGroupEnum.APP.name());
    }

    @Override
    protected ConfigData<AppData> fromJson(final JSONObject data) {
        return data.to(new TypeReference<ConfigData<AppData>>() {
        });
    }

    @Override
    protected boolean updateCacheIfNeed(final ConfigData<AppData> result) {
        return updateCacheIfNeed(result, ConfigGroupEnum.APP);
    }

    @Override
    public ConfigData<?> cacheConfigData() {
        return GROUP_CACHE.get(ConfigGroupEnum.APP);
    }

    @Override
    protected void refresh(final List<AppData> data) {
        if (CollectionUtils.isEmpty(data)) {
            LOG.info("clear all appAuth data cache");
            appDataSubscribers.forEach(AppDataSubscriber::refresh);
        } else {
            data.forEach(authData -> appDataSubscribers.forEach(subscriber -> subscriber.onSubscribe(authData)));
        }
    }
}
