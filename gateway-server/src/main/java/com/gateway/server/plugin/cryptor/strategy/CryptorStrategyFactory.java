package com.gateway.server.plugin.cryptor.strategy;

import com.gateway.server.context.GatewayContext;
import com.gateway.server.exception.GatewayException;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CryptorStrategyFactory {

    private static final Map<String, CryptorStrategy> STRATEGY_MAP = Maps.newConcurrentMap();

    public CryptorStrategyFactory(final ObjectProvider<List<CryptorStrategy>> strategyList) {
        for (CryptorStrategy cryptorStrategy : strategyList.getIfAvailable(Collections::emptyList)) {
            STRATEGY_MAP.put(cryptorStrategy.getClass().getSimpleName(), cryptorStrategy);
        }
    }

    public String encrypt(String data, GatewayContext context) {
        try {
            return STRATEGY_MAP.get(context.getCryptorType()).encrypt(data, context);
        } catch (Exception e) {
            throw new GatewayException("报文加密失败！");
        }
    }

    public String decrypt(String data, GatewayContext context) {
        try {
            return STRATEGY_MAP.get(context.getCryptorType()).decrypt(data, context);
        } catch (GatewayException e) {
            throw e;
        } catch (Exception e) {
            log.error("密文解密失败", e);
            throw new GatewayException("密文解密失败！");
        }
    }
}
