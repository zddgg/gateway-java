package com.gateway.server.sync.websocket.handler;

import com.alibaba.fastjson2.JSON;
import com.gateway.server.dto.UpstreamData;
import com.gateway.server.sync.UpstreamDataSubscriber;

import java.util.List;

/**
 * The type Auth data handler.
 */
public class UpstreamDataHandler extends AbstractDataHandler<UpstreamData> {

    private final List<UpstreamDataSubscriber> upstreamDataSubscribers;

    public UpstreamDataHandler(final List<UpstreamDataSubscriber> upstreamDataSubscribers) {
        this.upstreamDataSubscribers = upstreamDataSubscribers;
    }

    @Override
    public List<UpstreamData> convert(final String json) {
        return JSON.parseArray(json, UpstreamData.class);
    }

    @Override
    protected void doRefresh(final List<UpstreamData> dataList) {
        upstreamDataSubscribers.forEach(UpstreamDataSubscriber::refresh);
        dataList.forEach(appData -> upstreamDataSubscribers
                .forEach(upstreamDataSubscriber -> upstreamDataSubscriber.onSubscribe(appData)));
    }

    @Override
    protected void doUpdate(final List<UpstreamData> dataList) {
        dataList.forEach(appData -> upstreamDataSubscribers
                .forEach(upstreamDataSubscriber -> upstreamDataSubscriber.onSubscribe(appData)));
    }

    @Override
    protected void doDelete(final List<UpstreamData> dataList) {
        dataList.forEach(appData -> upstreamDataSubscribers
                .forEach(upstreamDataSubscriber -> upstreamDataSubscriber.unSubscribe(appData)));
    }
}
