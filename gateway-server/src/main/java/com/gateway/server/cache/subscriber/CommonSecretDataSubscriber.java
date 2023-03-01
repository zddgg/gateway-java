package com.gateway.server.cache.subscriber;

import com.gateway.server.cache.SecretDataCache;
import com.gateway.server.dto.SecretData;
import com.gateway.server.sync.SecretDataSubscriber;
import org.springframework.stereotype.Component;

@Component
public class CommonSecretDataSubscriber implements SecretDataSubscriber {
    @Override
    public void onSubscribe(SecretData data) {
        SecretDataCache.getInstance().cache(data);
    }

    @Override
    public void unSubscribe(SecretData data) {
        SecretDataCache.getInstance().remove(data);
    }
}
