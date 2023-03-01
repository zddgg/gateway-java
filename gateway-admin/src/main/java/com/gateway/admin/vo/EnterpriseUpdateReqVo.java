package com.gateway.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EnterpriseUpdateReqVo {

    @NotBlank(message = "企业编号不能为空")
    private String enterpriseId;

    @NotBlank(message = "企业名称不能为空")
    private String enterpriseName;
}
