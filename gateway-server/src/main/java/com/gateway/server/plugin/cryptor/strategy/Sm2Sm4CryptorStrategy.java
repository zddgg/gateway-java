package com.gateway.server.plugin.cryptor.strategy;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SM4;
import com.gateway.server.cache.AppDataCache;
import com.gateway.server.cache.SecretDataCache;
import com.gateway.server.context.GatewayContext;
import com.gateway.server.dto.AppData;
import com.gateway.server.dto.SecretData;
import com.gateway.server.exception.GatewayException;
import org.springframework.stereotype.Component;

@Component
public class Sm2Sm4CryptorStrategy implements CryptorStrategy {

    @Override
    public String decrypt(String data, GatewayContext context) {

        AppData appData = AppDataCache.getInstance().obtain(context.getAppKey());
        SecretData requestSecret = SecretDataCache.getInstance().obtain(appData.getRequestSecretId());
        SecretData responseSecret = SecretDataCache.getInstance().obtain(appData.getResponseSecretId());

        SM2 requestSm2;
        SM2 responseSm2;
        try {
            requestSm2 = new SM2(null, requestSecret.getPublicKey());
        } catch (Exception e) {
            throw new GatewayException("SM2密钥不符合规范");
        }

        try {
            responseSm2 = new SM2(responseSecret.getPrivateKey(), null);
        } catch (Exception e) {
            throw new GatewayException("SM2密钥不符合规范");
        }

        // 拼接验签字符串
        String signStr = buildSignStr(context.getAppKey(), context.getAppSecret(),
                context.getRequestTimestamp(), context.getRequestEncryptKey(), data);

        boolean verify;
        try {
            verify = requestSm2.verify(signStr.getBytes(), HexUtil.decodeHex(context.getRequestSign()));
        } catch (Exception e) {
            throw new GatewayException("SM2签名验证异常");
        }

        if (verify) {
            // 解密SM4密钥
            byte[] sm4Key;
            try {
                sm4Key = responseSm2.decrypt(HexUtil.decodeHex(context.getRequestEncryptKey()));
            } catch (Exception e) {
                throw new GatewayException("SM2解密SM4密钥失败");
            }

            SM4 sm4;
            try {
                sm4 = new SM4(sm4Key);
            } catch (Exception e) {
                throw new GatewayException("SM4密钥不符合规范");
            }

            try {
                return sm4.decryptStr(Base64.decode(data));
            } catch (Exception e) {
                throw new GatewayException("SM4解密报文失败");
            }
        } else {
            throw new GatewayException("SM2签名验证失败");
        }
    }

    @Override
    public String encrypt(String data, GatewayContext context) {

        AppData appData = AppDataCache.getInstance().obtain(context.getAppKey());
        SecretData requestSecret = SecretDataCache.getInstance().obtain(appData.getRequestSecretId());
        SecretData responseSecret = SecretDataCache.getInstance().obtain(appData.getResponseSecretId());

        SM2 requestSm2;
        SM2 responseSm2;
        try {
            requestSm2 = new SM2(requestSecret.getPrivateKey(), null);
        } catch (Exception e) {
            throw new GatewayException("SM2密钥不符合规范");
        }

        try {
            responseSm2 = new SM2(null, responseSecret.getPublicKey());
        } catch (Exception e) {
            throw new GatewayException("SM2密钥不符合规范");
        }

        // 生成sm4密钥
        SM4 sm4 = SmUtil.sm4();
        byte[] sm4Key = sm4.getSecretKey().getEncoded();

        // sm4加密报文
        String encText;
        try {
            encText = Base64.encode(sm4.encrypt(data.getBytes()));
        } catch (Exception e) {
            throw new GatewayException("SM4加密报文失败");
        }

        // SM2加密SM4密钥
        String sm4KeyStr;
        try {
            sm4KeyStr = responseSm2.encryptHex(sm4Key, KeyType.PublicKey);
        } catch (Exception e) {
            throw new GatewayException("SM2加密SM4密钥失败");
        }

        // 拼接签名字符串
        String signStr = buildSignStr(context.getAppKey(), context.getAppSecret(),
                context.getRequestTimestamp(), sm4KeyStr, encText);

        // 签名值
        String sign;
        try {
            sign = HexUtil.encodeHexStr(requestSm2.sign(signStr.getBytes()));
        } catch (Exception e) {
            throw new GatewayException("SM2签名生成失败");
        }

        context.setResponseEncryptKey(sm4KeyStr);
        context.setResponseSign(sign);
        return encText;
    }

    private String buildSignStr(String appKey, String appSecret, String timestamp, String encryptKey, String data) {
        return "appKey=" + appKey
                + "&appSecret=" + appSecret
                + "&timestamp" + timestamp
                + "&encryptKey" + encryptKey
                + "&data" + data;
    }
}
