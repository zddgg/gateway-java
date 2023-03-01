package com.gateway.server.enums;

import com.gateway.server.exception.GatewayException;

import java.util.Arrays;
import java.util.Objects;

/**
 * configuration group.
 */
public enum ConfigGroupEnum {

    /**
     * App auth config group enum.
     */
    APP,

    ROUTE,

    UPSTREAM,

    SECRET,
    ;

    /**
     * Acquire by name config group enum.
     *
     * @param name the name
     * @return the config group enum
     */
    public static ConfigGroupEnum acquireByName(final String name) {
        return Arrays.stream(ConfigGroupEnum.values())
                .filter(e -> Objects.equals(e.name(), name))
                .findFirst().orElseThrow(() -> new GatewayException(String.format(" this ConfigGroupEnum can not support %s", name)));
    }
}
