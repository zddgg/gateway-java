package com.gateway.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gateway.admin.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 应用路由表
 * &#064;TableName  application_route
 */
@Getter
@Setter
@TableName(value ="application_route")
public class ApplicationRoute extends BaseEntity<ApplicationRoute> {

    /**
     * 应用编号
     */
    @TableField(value = "app_id")
    private String appId;

    /**
     * 路由编号
     */
    @TableField(value = "route_id")
    private String routeId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}