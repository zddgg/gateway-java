package com.gateway.admin.constant;

public enum UpstreamType {
    NODE("0", "节点"),
    DISCOVERY("1", "服务发现"),
    ;

    public final String code;

    public final String desc;

    UpstreamType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
