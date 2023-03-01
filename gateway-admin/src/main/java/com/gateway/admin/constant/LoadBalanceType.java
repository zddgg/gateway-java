package com.gateway.admin.constant;

/**
 * 1、轮询法（Round Robin）
 * 2、加权轮询法（Weight Round Robin）
 * 3、随机法（Random）
 * 4、加权随机法（Weight Random）
 * 5、平滑加权轮询法（Smooth Weight Round Robin）
 * 6、源地址哈希法（Hash）
 * 7、最小连接数法（Least Connections）
 */
public enum LoadBalanceType {

    Round_Robin,
    Weight_Round_Robin,
    ;
}
