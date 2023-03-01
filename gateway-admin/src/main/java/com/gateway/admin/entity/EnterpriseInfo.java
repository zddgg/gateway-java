package com.gateway.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gateway.admin.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 企业信息表
 * <p>
 * &#064;TableName  enterprise_info
 */
@Getter
@Setter
@TableName(value = "enterprise_info")
public class EnterpriseInfo extends BaseEntity<EnterpriseInfo> implements Serializable {

    /**
     * 企业编号
     */
    @TableField(value = "enterprise_id")
    private String enterpriseId;

    /**
     * 企业名称
     */
    @TableField(value = "enterprise_name")
    private String enterpriseName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}