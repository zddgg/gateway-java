package com.gateway.admin.listener;

import com.gateway.admin.dto.cache.AppData;
import com.gateway.admin.dto.cache.RouteData;
import com.gateway.admin.dto.cache.SecretData;
import com.gateway.admin.dto.cache.UpstreamData;
import com.gateway.admin.enums.DataEventTypeEnum;

import java.util.List;

public interface DataChangedListener {

    default void onAppChanged(List<AppData> data, DataEventTypeEnum eventType) {
    }

    default void onRouteChanged(List<RouteData> data, DataEventTypeEnum eventType) {
    }

    default void onUpstreamChanged(List<UpstreamData> data, DataEventTypeEnum eventType) {
    }

    default void onSecretChanged(List<SecretData> data, DataEventTypeEnum eventType) {
    }
}
