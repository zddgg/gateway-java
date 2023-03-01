package com.gateway.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SecretUpdateReqVo {

    @NotBlank(message = "密钥编号不能为空")
    private String secretId;

    @NotBlank(message = "密钥名称不能为空")
    private String secretName;

    @NotBlank(message = "密钥类型不能为空")
    private String secretType;

    private String secretKey;

    private String publicKey;

    private String privateKey;

    @NotBlank(message = "编码方式不能为空")
    private String encodingType;

}
