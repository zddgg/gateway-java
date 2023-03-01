package com.gateway.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpstreamCreateReqVo {

    @NotBlank(message = "上游名称不能为空")
    private String upstreamName;

    @NotBlank(message = "上游类型不能为空")
    private String upstreamType;

    private String httpAddress;

    private String discoveryType;
}
