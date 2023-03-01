package com.gateway.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gateway.admin.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 节点信息表
 *
 * @TableName node_info
 */
@Getter
@Setter
@TableName(value = "node_info")
public class NodeInfo extends BaseEntity<NodeInfo> implements Serializable {

    /**
     * host
     */
    @TableField(value = "host")
    private String host;

    /**
     * port
     */
    @TableField(value = "port")
    private String port;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}