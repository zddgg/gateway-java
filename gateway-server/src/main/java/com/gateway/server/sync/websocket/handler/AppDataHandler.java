package com.gateway.server.sync.websocket.handler;

import com.alibaba.fastjson2.JSON;
import com.gateway.server.dto.AppData;
import com.gateway.server.sync.AppDataSubscriber;

import java.util.List;

/**
 * The type Auth data handler.
 */
public class AppDataHandler extends AbstractDataHandler<AppData> {

    private final List<AppDataSubscriber> appDataSubscribers;

    public AppDataHandler(final List<AppDataSubscriber> appDataSubscribers) {
        this.appDataSubscribers = appDataSubscribers;
    }

    @Override
    public List<AppData> convert(final String json) {
        return JSON.parseArray(json, AppData.class);
    }

    @Override
    protected void doRefresh(final List<AppData> dataList) {
        appDataSubscribers.forEach(AppDataSubscriber::refresh);
        dataList.forEach(appData -> appDataSubscribers.forEach(appDataSubscriber -> appDataSubscriber.onSubscribe(appData)));
    }

    @Override
    protected void doUpdate(final List<AppData> dataList) {
        dataList.forEach(appData -> appDataSubscribers.forEach(appDataSubscriber -> appDataSubscriber.onSubscribe(appData)));
    }

    @Override
    protected void doDelete(final List<AppData> dataList) {
        dataList.forEach(appData -> appDataSubscribers.forEach(appDataSubscriber -> appDataSubscriber.unSubscribe(appData)));
    }
}
