package com.gateway.server.cache;

import com.gateway.server.dto.UpstreamData;
import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;

public final class UpstreamDataCache implements DataCache<UpstreamData> {

    private static final UpstreamDataCache INSTANCE = new UpstreamDataCache();

    private static final ConcurrentMap<String, UpstreamData> UPSTREAM_MAP = Maps.newConcurrentMap();

    private UpstreamDataCache() {
    }

    public static UpstreamDataCache getInstance() {
        return INSTANCE;
    }

    @Override
    public void cache(final UpstreamData data) {
        UPSTREAM_MAP.put(data.getUpstreamId(), data);
    }

    @Override
    public void remove(final UpstreamData data) {
        UPSTREAM_MAP.remove(data.getUpstreamId());
    }

    @Override
    public UpstreamData obtain(final String key) {
        return UPSTREAM_MAP.get(key);
    }
}
