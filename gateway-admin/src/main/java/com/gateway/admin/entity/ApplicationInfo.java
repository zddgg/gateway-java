package com.gateway.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gateway.admin.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 应用信息表
 * <p>
 * &#064;TableName  application_info
 */
@Getter
@Setter
@TableName(value = "application_info")
public class ApplicationInfo extends BaseEntity<ApplicationInfo> {

    @TableField(value = "app_id")
    private String appId;

    @TableField(value = "app_name")
    private String appName;

    @TableField(value = "app_key")
    private String appKey;

    @TableField(value = "app_secret")
    private String appSecret;

    @TableField(value = "enterprise_id")
    private String enterpriseId;

    @TableField(value = "cryptor_enable")
    private String cryptorEnable;

    @TableField(value = "secret_id")
    private String secretId;

    @TableField(value = "request_secret_id")
    private String requestSecretId;

    @TableField(value = "response_secret_id")
    private String responseSecretId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}