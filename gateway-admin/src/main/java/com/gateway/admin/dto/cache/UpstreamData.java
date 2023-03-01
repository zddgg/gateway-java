package com.gateway.admin.dto.cache;

import com.gateway.admin.entity.UpstreamInfo;
import lombok.Data;

@Data
public class UpstreamData {

    private String upstreamId;

    private String upstreamName;

    private String upstreamType;

    private String httpAddress;

    private String discoveryType;

    public static UpstreamData convert(UpstreamInfo upstreamInfo) {
        UpstreamData upstreamData = new UpstreamData();
        upstreamData.setUpstreamId(upstreamInfo.getUpstreamId());
        upstreamData.setUpstreamName(upstreamInfo.getUpstreamName());
        upstreamData.setUpstreamType(upstreamInfo.getUpstreamType());
        upstreamData.setHttpAddress(upstreamInfo.getHttpAddress());
        upstreamData.setDiscoveryType(upstreamInfo.getDiscoveryType());
        return upstreamData;
    }
}
