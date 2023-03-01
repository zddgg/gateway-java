package com.gateway.server.dto;

import lombok.Data;

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
}
