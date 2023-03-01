package com.gateway.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SecretCreateReqVo {

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
