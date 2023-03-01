package com.gateway.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gateway.admin.entity.SecretInfo;
import com.gateway.admin.mapper.SecretInfoMapper;
import com.gateway.admin.service.SecretInfoService;
import org.springframework.stereotype.Service;

/**
* @author 86239
* @description 针对表【secret_info(密钥信息表)】的数据库操作Service实现
* @createDate 2023-01-09 14:35:04
*/
@Service
public class SecretInfoServiceImpl extends ServiceImpl<SecretInfoMapper, SecretInfo>
    implements SecretInfoService{

}




