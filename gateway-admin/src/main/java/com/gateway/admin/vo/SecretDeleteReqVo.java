package com.gateway.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SecretDeleteReqVo {

    @NotBlank(message = "密钥编号不能为空")
    private String secretId;
}
