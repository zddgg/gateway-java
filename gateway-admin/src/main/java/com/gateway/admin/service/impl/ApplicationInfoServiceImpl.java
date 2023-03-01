package com.gateway.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gateway.admin.entity.ApplicationInfo;
import com.gateway.admin.mapper.ApplicationInfoMapper;
import com.gateway.admin.service.ApplicationInfoService;
import org.springframework.stereotype.Service;

/**
* @author 86239
* @description 针对表【application_info(应用信息表)】的数据库操作Service实现
* @createDate 2022-12-15 18:59:16
*/
@Service
public class ApplicationInfoServiceImpl extends ServiceImpl<ApplicationInfoMapper, ApplicationInfo>
    implements ApplicationInfoService{

}




