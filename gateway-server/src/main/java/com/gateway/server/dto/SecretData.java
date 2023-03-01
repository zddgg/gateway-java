package com.gateway.server.dto;

import lombok.Data;

@Data
public class SecretData {

    private String secretId;

    private String secretName;

    private String secretType;

    private String secretKey;

    private String publicKey;

    private String privateKey;

    private String encodingType;
}
