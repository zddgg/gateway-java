package com.gateway.server.cache;

import com.gateway.server.dto.RouteData;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentMap;

@Slf4j
public final class RouteDataCache implements DataCache<RouteData> {

    private static final RouteDataCache INSTANCE = new RouteDataCache();

    public static final ConcurrentMap<String, RouteData> ROUTE_MAP = Maps.newConcurrentMap();

    private RouteDataCache() {
    }

    public static RouteDataCache getInstance() {
        return INSTANCE;
    }

    public void cache(final RouteData data) {
        log.info("accept cache data: {}", data);
        ROUTE_MAP.put(data.getRoutePath(), data);
    }

    public void remove(final RouteData data) {
        ROUTE_MAP.remove(data.getRoutePath());
    }

    @Override
    public RouteData obtain(final String key) {
        return ROUTE_MAP.get(key);
    }
}
