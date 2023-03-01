package com.gateway.server.sync;

import com.gateway.server.dto.AppData;

public interface AppDataSubscriber {

    void onSubscribe(AppData data);

    void unSubscribe(AppData data);

    default void refresh() {
    }
}
