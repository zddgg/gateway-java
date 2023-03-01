package com.gateway.server.sync;

import com.gateway.server.dto.SecretData;

public interface SecretDataSubscriber {

    void onSubscribe(SecretData data);

    void unSubscribe(SecretData data);

    default void refresh() {
    }
}
