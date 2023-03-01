package com.gateway.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ApplicationCreateReqVo {

    @NotBlank(message = "应用名称不能为空")
    private String appName;

    private String appKey;

    private String appSecret;

    @NotBlank(message = "企业编号不能为空")
    private String enterpriseId;

    private String cryptorEnable;

    private String secretId;

    private String requestSecretId;

    private String responseSecretId;
}
