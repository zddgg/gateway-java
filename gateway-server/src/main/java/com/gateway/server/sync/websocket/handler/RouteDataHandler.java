package com.gateway.server.sync.websocket.handler;

import com.alibaba.fastjson2.JSON;
import com.gateway.server.dto.RouteData;
import com.gateway.server.sync.RouteDataSubscriber;

import java.util.List;

/**
 * The type Auth data handler.
 */
public class RouteDataHandler extends AbstractDataHandler<RouteData> {

    private final List<RouteDataSubscriber> routeDataSubscribers;

    public RouteDataHandler(final List<RouteDataSubscriber> routeDataSubscribers) {
        this.routeDataSubscribers = routeDataSubscribers;
    }

    @Override
    public List<RouteData> convert(final String json) {
        return JSON.parseArray(json, RouteData.class);
    }

    @Override
    protected void doRefresh(final List<RouteData> dataList) {
        routeDataSubscribers.forEach(RouteDataSubscriber::refresh);
        dataList.forEach(appData ->
                routeDataSubscribers.forEach(routeDataSubscriber ->
                        routeDataSubscriber.onSubscribe(appData)
                )
        );
    }

    @Override
    protected void doUpdate(final List<RouteData> dataList) {
        dataList.forEach(appData ->
                routeDataSubscribers.forEach(routeDataSubscriber ->
                        routeDataSubscriber.onSubscribe(appData)
                )
        );
    }

    @Override
    protected void doDelete(final List<RouteData> dataList) {
        dataList.forEach(appData ->
                routeDataSubscribers.forEach(routeDataSubscriber ->
                        routeDataSubscriber.unSubscribe(appData)
                )
        );
    }
}
