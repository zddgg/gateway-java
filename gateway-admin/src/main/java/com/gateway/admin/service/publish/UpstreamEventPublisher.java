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

package com.gateway.admin.service.publish;

import com.gateway.admin.dto.ConfigGroupEnum;
import com.gateway.admin.dto.cache.UpstreamData;
import com.gateway.admin.entity.UpstreamInfo;
import com.gateway.admin.enums.DataEventTypeEnum;
import com.gateway.admin.listener.DataChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UpstreamEventPublisher implements DataChangedEventPublisher<UpstreamInfo> {

    private final ApplicationEventPublisher publisher;

    public UpstreamEventPublisher(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onCreated(UpstreamInfo data) {
        publisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.UPSTREAM, DataEventTypeEnum.CREATE,
                Collections.singletonList(UpstreamData.convert(data))));
    }

    @Override
    public void onUpdated(UpstreamInfo data) {
        publisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.UPSTREAM, DataEventTypeEnum.UPDATE,
                Collections.singletonList(UpstreamData.convert(data))));
    }

    @Override
    public void onDeleted(UpstreamInfo data) {
        publisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.UPSTREAM, DataEventTypeEnum.DELETE,
                Collections.singletonList(UpstreamData.convert(data))));
    }
}
