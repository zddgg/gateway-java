package com.gateway.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gateway.admin.entity.NodeInfo;
import com.gateway.admin.mapper.NodeInfoMapper;
import com.gateway.admin.service.NodeInfoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author 86239
 * @description 针对表【node_info(节点信息表)】的数据库操作Service实现
 * @createDate 2022-12-25 23:11:39
 */
@Service
public class NodeInfoServiceImpl extends ServiceImpl<NodeInfoMapper, NodeInfo>
        implements NodeInfoService {

    @Override
    public void registerIfNotExist(String host, String port) {
        NodeInfo nodeInfo = this.getOne(
                new LambdaUpdateWrapper<NodeInfo>()
                        .eq(NodeInfo::getHost, host)
                        .eq(NodeInfo::getPort, port));
        if (Objects.isNull(nodeInfo)) {
            NodeInfo saveInfo = new NodeInfo();
            saveInfo.setHost(host);
            saveInfo.setPort(port);
            saveInfo.setCreateId("xx");
            saveInfo.setCreateTime(LocalDateTime.now());
            saveInfo.setUpdateId("xx");
            saveInfo.setUpdateTime(LocalDateTime.now());
            this.save(saveInfo);
        }
    }
}




