package com.gateway.admin.dto.cache;

import com.gateway.admin.entity.SecretInfo;
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

    public static SecretData convert(SecretInfo secretInfo) {
        SecretData secretData = new SecretData();
        secretData.setSecretId(secretInfo.getSecretId());
        secretData.setSecretName(secretInfo.getSecretName());
        secretData.setSecretType(secretInfo.getSecretType());
        secretData.setSecretKey(secretInfo.getSecretKey());
        secretData.setPublicKey(secretInfo.getPublicKey());
        secretData.setPrivateKey(secretInfo.getPrivateKey());
        secretData.setEncodingType(secretInfo.getEncodingType());
        return secretData;
    }
}
