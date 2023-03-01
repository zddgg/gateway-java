package com.gateway.server.sync.websocket.handler;

import com.alibaba.fastjson2.JSON;
import com.gateway.server.dto.SecretData;
import com.gateway.server.sync.SecretDataSubscriber;

import java.util.List;

public class SecretDataHandler extends AbstractDataHandler<SecretData> {

    private final List<SecretDataSubscriber> secretDataSubscribers;

    public SecretDataHandler(final List<SecretDataSubscriber> secretDataSubscribers) {
        this.secretDataSubscribers = secretDataSubscribers;
    }

    @Override
    public List<SecretData> convert(final String json) {
        return JSON.parseArray(json, SecretData.class);
    }

    @Override
    protected void doRefresh(final List<SecretData> dataList) {
        secretDataSubscribers.forEach(SecretDataSubscriber::refresh);
        dataList.forEach(secretData -> secretDataSubscribers.forEach(appDataSubscriber -> appDataSubscriber.onSubscribe(secretData)));
    }

    @Override
    protected void doUpdate(final List<SecretData> dataList) {
        dataList.forEach(secretData -> secretDataSubscribers.forEach(appDataSubscriber -> appDataSubscriber.onSubscribe(secretData)));
    }

    @Override
    protected void doDelete(final List<SecretData> dataList) {
        dataList.forEach(secretData -> secretDataSubscribers.forEach(appDataSubscriber -> appDataSubscriber.unSubscribe(secretData)));
    }
}
