package com.gateway.server.sync;

import com.gateway.server.dto.UpstreamData;

public interface UpstreamDataSubscriber {

    void onSubscribe(UpstreamData data);

    void unSubscribe(UpstreamData data);

    default void refresh() {
    }
}
