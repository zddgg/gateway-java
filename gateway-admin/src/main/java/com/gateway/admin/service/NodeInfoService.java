package com.gateway.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gateway.admin.entity.NodeInfo;

/**
* @author 86239
* @description 针对表【node_info(节点信息表)】的数据库操作Service
* @createDate 2022-12-25 23:11:39
*/
public interface NodeInfoService extends IService<NodeInfo> {

    void registerIfNotExist(String host, String port);

}
