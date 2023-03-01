package com.gateway.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpstreamDeleteReqVo {

    @NotBlank(message = "上游编号不能为空")
    private String upstreamId;
}
