package com.gateway.server.context;

import lombok.Data;

@Data
public class GatewayContext {

    private String rpcType;

    private String httpMethod;

    private String path;

    private String version;

    private String appKey;

    private String appSecret;

    private String cryptorType;


    /**
     * request
     */
    private String requestTimestamp;

    private String requestSign;

    private String requestEncryptKey;

    private String requestSm2PrivateKey;

    private String requestSm2PublicKey;


    /**
     * response
     */
    private String responseTimestamp;

    private String responseSign;

    private String responseEncryptKey;

    private String responseSm2PrivateKey;

    private String responseSm2PublicKey;

}
