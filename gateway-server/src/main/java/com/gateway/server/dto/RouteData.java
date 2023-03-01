package com.gateway.server.dto;

import lombok.Data;

@Data
public class RouteData {

    private String routeId;

    private String routeName;

    private String routePath;

    private String routeVersion;

    private String upstreamId;
}
