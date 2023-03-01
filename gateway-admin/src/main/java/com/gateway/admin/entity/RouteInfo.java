package com.gateway.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gateway.admin.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * API信息表
 * &#064;TableName  route_info
 */
@Getter
@Setter
@TableName(value ="route_info")
public class RouteInfo extends BaseEntity<RouteInfo> {

    /**
     * 路由编号
     */
    @TableField(value = "route_id")
    private String routeId;

    /**
     * 路由名称
     */
    @TableField(value = "route_name")
    private String routeName;

    @TableField(value = "route_path")
    private String routePath;

    /**
     * 路由版本
     */
    @TableField(value = "route_version")
    private String routeVersion;

    /**
     * 上游编号
     */
    @TableField(value = "upstream_id")
    private String upstreamId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}