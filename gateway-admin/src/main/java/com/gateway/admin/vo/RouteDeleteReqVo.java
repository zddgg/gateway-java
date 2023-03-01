package com.gateway.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RouteDeleteReqVo {

    @NotBlank(message = "路由编号不能为空")
    private String routeId;
}
