package com.gateway.server.plugin.cryptor.strategy;

import com.gateway.server.context.GatewayContext;

public interface CryptorStrategy {

    String decrypt(String data, GatewayContext context) throws Exception;

    String encrypt(String data, GatewayContext context) throws Exception;
}
