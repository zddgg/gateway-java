package com.gateway.server.cache;

import com.gateway.server.dto.SecretData;
import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;

public final class SecretDataCache implements DataCache<SecretData> {

    private static final SecretDataCache INSTANCE = new SecretDataCache();

    private static final ConcurrentMap<String, SecretData> Secret_MAP = Maps.newConcurrentMap();

    private SecretDataCache() {
    }

    public static SecretDataCache getInstance() {
        return INSTANCE;
    }

    @Override
    public void cache(final SecretData data) {
        Secret_MAP.put(data.getSecretId(), data);
    }

    @Override
    public void remove(final SecretData data) {
        Secret_MAP.remove(data.getSecretId());
    }

    @Override
    public SecretData obtain(final String key) {
        return Secret_MAP.get(key);
    }
}
