package com.gateway.server.sync.http.refresh;

import com.alibaba.fastjson2.JSONObject;
import com.gateway.server.dto.ConfigData;
import com.gateway.server.enums.ConfigGroupEnum;
import com.gateway.server.sync.AppDataSubscriber;

import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

public class DataRefreshFactory {

    private static final EnumMap<ConfigGroupEnum, DataRefresh> ENUM_MAP = new EnumMap<>(ConfigGroupEnum.class);

    public DataRefreshFactory(final List<AppDataSubscriber> appDataSubscribers) {
        ENUM_MAP.put(ConfigGroupEnum.APP, new AppDataRefresh(appDataSubscribers));
    }

    /**
     * Executor.
     *
     * @param data the data
     * @return the boolean
     */
    public boolean executor(final JSONObject data) {
        List<Boolean> result = ENUM_MAP.values().stream()
                .map(dataRefresh -> dataRefresh.refresh(data))
                .collect(Collectors.toList());
        return result.stream().anyMatch(Boolean.TRUE::equals);
    }

    /**
     * Cache config data.
     *
     * @param group the group
     * @return the config data
     */
    public ConfigData<?> cacheConfigData(final ConfigGroupEnum group) {
        return ENUM_MAP.get(group).cacheConfigData();
    }
}
