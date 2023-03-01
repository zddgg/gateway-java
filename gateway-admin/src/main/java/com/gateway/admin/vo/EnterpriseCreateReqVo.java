package com.gateway.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EnterpriseCreateReqVo {

    @NotBlank(message = "企业名称不能为空")
    private String enterpriseName;

}
