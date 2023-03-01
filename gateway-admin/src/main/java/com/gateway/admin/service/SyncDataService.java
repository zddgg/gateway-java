package com.gateway.admin.service;

import com.gateway.admin.enums.DataEventTypeEnum;

/**
 * The interface Sync data service.
 */
public interface SyncDataService {

    /**
     * Sync all boolean.
     *
     * @param type the type
     * @return the boolean
     */
    boolean syncAll(DataEventTypeEnum type);

    /**
     * Sync plugin data boolean.
     *
     * @param pluginId the plugin id
     * @return the boolean
     */
    boolean syncPluginData(String pluginId);

}
