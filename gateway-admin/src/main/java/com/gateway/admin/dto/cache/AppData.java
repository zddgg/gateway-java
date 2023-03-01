package com.gateway.admin.dto.cache;

import com.gateway.admin.entity.ApplicationInfo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class AppData {

    private String appId;

    private String appKey;

    private String appSecret;

    private Boolean enabled;

    private Boolean cryptorEnable;

    private String secretId;

    private String requestSecretId;

    private String responseSecretId;

    public static AppData convert(ApplicationInfo applicationInfo) {
        AppData appData = new AppData();
        appData.setAppId(applicationInfo.getAppId());
        appData.setAppKey(applicationInfo.getAppKey());
        appData.setAppSecret(applicationInfo.getAppSecret());
        appData.setEnabled(StringUtils.equals(applicationInfo.getCryptorEnable(), "1"));
        appData.setCryptorEnable(StringUtils.equals(applicationInfo.getCryptorEnable(), "1"));
        appData.setSecretId(applicationInfo.getSecretId());
        appData.setRequestSecretId(applicationInfo.getRequestSecretId());
        appData.setResponseSecretId(applicationInfo.getResponseSecretId());
        return appData;
    }
}
