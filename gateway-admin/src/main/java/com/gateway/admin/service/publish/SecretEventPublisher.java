package com.gateway.admin.service.publish;

import com.gateway.admin.dto.ConfigGroupEnum;
import com.gateway.admin.dto.cache.SecretData;
import com.gateway.admin.entity.SecretInfo;
import com.gateway.admin.enums.DataEventTypeEnum;
import com.gateway.admin.listener.DataChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SecretEventPublisher implements DataChangedEventPublisher<SecretInfo> {

    private final ApplicationEventPublisher publisher;

    public SecretEventPublisher(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onCreated(SecretInfo data) {
        publisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.SECRET, DataEventTypeEnum.CREATE,
                Collections.singletonList(SecretData.convert(data))));
    }

    @Override
    public void onUpdated(SecretInfo data) {
        publisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.SECRET, DataEventTypeEnum.UPDATE,
                Collections.singletonList(SecretData.convert(data))));
    }

    @Override
    public void onDeleted(SecretInfo data) {
        publisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.SECRET, DataEventTypeEnum.DELETE,
                Collections.singletonList(SecretData.convert(data))));
    }
}
