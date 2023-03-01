package com.gateway.server.cache.subscriber;

import com.gateway.server.cache.AppDataCache;
import com.gateway.server.dto.AppData;
import com.gateway.server.sync.AppDataSubscriber;
import org.springframework.stereotype.Component;

@Component
public class CommonAppDataSubscriber implements AppDataSubscriber {
    @Override
    public void onSubscribe(AppData data) {
        AppDataCache.getInstance().cache(data);
    }

    @Override
    public void unSubscribe(AppData data) {
        AppDataCache.getInstance().remove(data);
    }
}
