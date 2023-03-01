package com.gateway.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gateway.admin.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 密钥信息表
 *
 * @TableName secret_info
 */
@Getter
@Setter
@TableName(value = "secret_info")
public class SecretInfo extends BaseEntity<SecretInfo> implements Serializable {

    /**
     * 密钥编号
     */
    @TableField(value = "secret_id")
    private String secretId;

    /**
     * 密钥名称
     */
    @TableField(value = "secret_name")
    private String secretName;

    /**
     * 密钥类型 1-对称密钥， 2-非对称密钥
     */
    @TableField(value = "secret_type")
    private String secretType;

    /**
     * 密钥值
     */
    @TableField(value = "secret_key")
    private String secretKey;

    /**
     * 公钥
     */
    @TableField(value = "public_key")
    private String publicKey;

    /**
     * 私钥
     */
    @TableField(value = "private_key")
    private String privateKey;

    /**
     * 编码类型
     */
    @TableField(value = "encoding_type")
    private String encodingType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}