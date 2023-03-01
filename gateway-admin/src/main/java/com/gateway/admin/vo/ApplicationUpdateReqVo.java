package com.gateway.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ApplicationUpdateReqVo {

    @NotBlank(message = "应用编号不能为空")
    private String appId;

    @NotBlank(message = "应用名称不能为空")
    private String appName;

    private String enterpriseId;

    private String cryptorEnable;

    private String secretId;

    private String requestSecretId;

    private String responseSecretId;

}
