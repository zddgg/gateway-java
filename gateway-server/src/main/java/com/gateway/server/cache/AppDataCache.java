package com.gateway.server.cache;

import com.gateway.server.dto.AppData;
import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;

public final class AppDataCache implements DataCache<AppData> {

    private static final AppDataCache INSTANCE = new AppDataCache();

    private static final ConcurrentMap<String, AppData> APP_MAP = Maps.newConcurrentMap();

    private AppDataCache() {
    }

    public static AppDataCache getInstance() {
        return INSTANCE;
    }

    @Override
    public void cache(final AppData data) {
        APP_MAP.put(data.getAppKey(), data);
    }

    @Override
    public void remove(final AppData data) {
        APP_MAP.remove(data.getAppKey());
    }

    @Override
    public AppData obtain(final String key) {
        return APP_MAP.get(key);
    }
}
