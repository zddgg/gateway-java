package com.gateway.server.cache.subscriber;

import com.gateway.server.cache.RouteDataCache;
import com.gateway.server.dto.RouteData;
import com.gateway.server.sync.RouteDataSubscriber;
import org.springframework.stereotype.Component;

@Component
public class CommonRouteDataSubscriber implements RouteDataSubscriber {
    @Override
    public void onSubscribe(RouteData data) {
        RouteDataCache.getInstance().cache(data);
    }

    @Override
    public void unSubscribe(RouteData data) {
        RouteDataCache.getInstance().remove(data);
    }
}
