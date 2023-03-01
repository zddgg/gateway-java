package com.gateway.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ApplicationDeleteReqVo {

    @NotBlank(message = "应用编号不能为空")
    private String appId;
}
