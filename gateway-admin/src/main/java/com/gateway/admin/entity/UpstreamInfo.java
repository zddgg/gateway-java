package com.gateway.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gateway.admin.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 上游信息表
 * <p>
 * &#064;TableName  upstream_info
 */
@Getter
@Setter
@TableName(value = "upstream_info")
public class UpstreamInfo extends BaseEntity<UpstreamInfo> {

    /**
     * 上游编号
     */
    @TableField(value = "upstream_id")
    private String upstreamId;

    /**
     * 上游名称
     */
    @TableField(value = "upstream_name")
    private String upstreamName;

    /**
     * 上游类型
     */
    @TableField(value = "upstream_type")
    private String upstreamType;

    /**
     * 上游地址
     */
    @TableField(value = "http_address")
    private String httpAddress;

    /**
     * 服务发现类型
     */
    @TableField(value = "discovery_type")
    private String discoveryType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}