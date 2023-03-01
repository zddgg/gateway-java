package com.gateway.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RouteCreateReqVo {

    @NotBlank(message = "路由名称不能为空")
    private String routeName;

    @NotBlank(message = "路由路径不能为空")
    private String routePath;

    private String routeVersion;

    @NotBlank(message = "上游编号不能为空")
    private String upstreamId;
}
