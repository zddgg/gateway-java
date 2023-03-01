package com.gateway.admin.dto.cache;

import com.gateway.admin.entity.RouteInfo;
import lombok.Data;

@Data
public class RouteData {

    private String routeId;

    private String routeName;

    private String routePath;

    private String routeVersion;

    private String upstreamId;

    public static RouteData convert(RouteInfo routeInfo) {
        RouteData routeData = new RouteData();
        routeData.setRouteId(routeInfo.getRouteId());
        routeData.setRouteName(routeInfo.getRouteName());
        routeData.setRoutePath(routeInfo.getRoutePath());
        routeData.setRouteVersion(routeInfo.getRouteVersion());
        routeData.setUpstreamId(routeInfo.getUpstreamId());
        return routeData;
    }
}
