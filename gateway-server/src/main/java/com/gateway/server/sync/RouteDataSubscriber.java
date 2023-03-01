package com.gateway.server.sync;

import com.gateway.server.dto.RouteData;

public interface RouteDataSubscriber {

    void onSubscribe(RouteData data);

    void unSubscribe(RouteData data);

    default void refresh() {
    }
}
