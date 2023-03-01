package com.gateway.server.dto;

import lombok.Data;

@Data
public class UpstreamData {

    private String upstreamId;

    private String upstreamName;

    private String upstreamType;

    private String httpAddress;

    private String discoveryType;
}
