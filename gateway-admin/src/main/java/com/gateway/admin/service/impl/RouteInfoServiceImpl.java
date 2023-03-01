package com.gateway.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gateway.admin.entity.RouteInfo;
import com.gateway.admin.mapper.RouteInfoMapper;
import com.gateway.admin.service.RouteInfoService;
import org.springframework.stereotype.Service;

/**
* @author 86239
* @description 针对表【route_info(API信息表)】的数据库操作Service实现
* @createDate 2022-12-15 18:59:16
*/
@Service
public class RouteInfoServiceImpl extends ServiceImpl<RouteInfoMapper, RouteInfo>
    implements RouteInfoService{

}




