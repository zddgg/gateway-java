package com.gateway.server.cache.subscriber;

import com.gateway.server.cache.UpstreamDataCache;
import com.gateway.server.dto.UpstreamData;
import com.gateway.server.sync.UpstreamDataSubscriber;
import org.springframework.stereotype.Component;

@Component
public class CommonUpstreamDataSubscriber implements UpstreamDataSubscriber {
    @Override
    public void onSubscribe(UpstreamData data) {
        UpstreamDataCache.getInstance().cache(data);
    }

    @Override
    public void unSubscribe(UpstreamData data) {
        UpstreamDataCache.getInstance().remove(data);
    }
}
