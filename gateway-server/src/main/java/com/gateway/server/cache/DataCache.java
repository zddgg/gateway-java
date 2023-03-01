package com.gateway.server.cache;

public interface DataCache<T> {

    public void cache(final T data);

    public void remove(final T data);

    public T obtain(final String key);
}
