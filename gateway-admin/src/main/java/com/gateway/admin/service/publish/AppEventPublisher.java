package com.gateway.admin.service.publish;

import com.gateway.admin.dto.ConfigGroupEnum;
import com.gateway.admin.dto.cache.AppData;
import com.gateway.admin.entity.ApplicationInfo;
import com.gateway.admin.enums.DataEventTypeEnum;
import com.gateway.admin.listener.DataChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AppEventPublisher implements DataChangedEventPublisher<ApplicationInfo> {

    private final ApplicationEventPublisher publisher;

    public AppEventPublisher(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onCreated(ApplicationInfo data) {
        publisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.APP, DataEventTypeEnum.CREATE,
                Collections.singletonList(AppData.convert(data))));
    }

    @Override
    public void onUpdated(ApplicationInfo data) {
        publisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.APP, DataEventTypeEnum.UPDATE,
                Collections.singletonList(AppData.convert(data))));
    }

    @Override
    public void onDeleted(ApplicationInfo data) {
        publisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.APP, DataEventTypeEnum.DELETE,
                Collections.singletonList(AppData.convert(data))));
    }
}
