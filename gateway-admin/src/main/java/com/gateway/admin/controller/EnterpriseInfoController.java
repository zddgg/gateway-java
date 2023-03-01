package com.gateway.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gateway.admin.entity.EnterpriseInfo;
import com.gateway.admin.service.EnterpriseInfoService;
import com.gateway.admin.utils.IdUtil;
import com.gateway.admin.vo.EnterpriseCreateReqVo;
import com.gateway.admin.vo.EnterpriseDeleteReqVo;
import com.gateway.admin.vo.EnterpriseUpdateReqVo;
import com.voidtime.mall.common.request.PaginationReq;
import com.voidtime.mall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("enterprise")
public class EnterpriseInfoController {

    private final EnterpriseInfoService enterpriseInfoService;

    public EnterpriseInfoController(EnterpriseInfoService enterpriseInfoService) {
        this.enterpriseInfoService = enterpriseInfoService;
    }


    @PostMapping("list")
    public Result<Page<EnterpriseInfo>> list(@RequestBody PaginationReq req) {
        Page<EnterpriseInfo> page = enterpriseInfoService.page(Page.of(req.getCurrent(), req.getPageSize()));
        return Result.success(page);
    }


    @PostMapping("create")
    public Result<Object> create(@RequestBody @Validated EnterpriseCreateReqVo reqVo) {
        EnterpriseInfo enterpriseInfo = new EnterpriseInfo();
        enterpriseInfo.setEnterpriseId(IdUtil.getId());
        enterpriseInfo.setEnterpriseName(reqVo.getEnterpriseName());
        enterpriseInfo.setCreateId("xx");
        enterpriseInfo.setCreateTime(LocalDateTime.now());
        enterpriseInfo.setUpdateId("xx");
        enterpriseInfo.setUpdateTime(LocalDateTime.now());
        enterpriseInfoService.save(enterpriseInfo);
        return Result.success();
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody @Validated EnterpriseDeleteReqVo reqVo) {
        EnterpriseInfo enterpriseInfo = enterpriseInfoService.getOne(
                new LambdaQueryWrapper<EnterpriseInfo>()
                        .eq(EnterpriseInfo::getEnterpriseId, reqVo.getEnterpriseId()));
        enterpriseInfoService.removeById(enterpriseInfo);
        return Result.success();
    }

    @PostMapping("update")
    public Result<Object> update(@RequestBody @Validated EnterpriseUpdateReqVo reqVo) {
        EnterpriseInfo enterpriseInfo = enterpriseInfoService.getOne(
                new LambdaQueryWrapper<EnterpriseInfo>()
                        .eq(EnterpriseInfo::getEnterpriseId, reqVo.getEnterpriseId()));
        enterpriseInfo.setEnterpriseName(reqVo.getEnterpriseName());
        enterpriseInfo.setUpdateTime(LocalDateTime.now());
        enterpriseInfoService.updateById(enterpriseInfo);
        return Result.success();
    }

    @PostMapping("detail")
    public Result<EnterpriseInfo> detail(@RequestBody @Validated EnterpriseDeleteReqVo reqVo) {
        return Result.success(enterpriseInfoService.getOne(
                new LambdaQueryWrapper<EnterpriseInfo>()
                        .eq(EnterpriseInfo::getEnterpriseId, reqVo.getEnterpriseId())));
    }
}
