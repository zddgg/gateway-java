package com.gateway.admin.utils;

import java.net.URI;

public class URIUtil {
    public static String getParameter(URI uri, String parameter) {
        String[] parameters = uri.getRawQuery().split("&");
        for (String p : parameters) {
            if (p.contains(parameter)) {
                return p.substring(p.lastIndexOf("=") + 1);
            }
        }
        return null;
    }

    public static String getNodeKey(URI uri) {
        String host = URIUtil.getParameter(uri, "host");
        String port = URIUtil.getParameter(uri, "port");
        return getNodeKey(host, port);
    }

    public static String getNodeKey(String host, String port) {
        return host + ":" + port;
    }
}
