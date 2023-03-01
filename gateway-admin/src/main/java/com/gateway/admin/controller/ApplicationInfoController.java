package com.gateway.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gateway.admin.entity.ApplicationInfo;
import com.gateway.admin.service.ApplicationInfoService;
import com.gateway.admin.service.publish.AppEventPublisher;
import com.gateway.admin.utils.IdUtil;
import com.gateway.admin.vo.ApplicationCreateReqVo;
import com.gateway.admin.vo.ApplicationDeleteReqVo;
import com.gateway.admin.vo.ApplicationUpdateReqVo;
import com.voidtime.mall.common.request.PaginationReq;
import com.voidtime.mall.common.response.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("application")
public class ApplicationInfoController {

    private final ApplicationInfoService applicationInfoService;

    private final AppEventPublisher appEventPublisher;

    public ApplicationInfoController(ApplicationInfoService applicationInfoService,
                                     AppEventPublisher appEventPublisher) {
        this.applicationInfoService = applicationInfoService;
        this.appEventPublisher = appEventPublisher;
    }

    @PostMapping("list")
    public Result<Page<ApplicationInfo>> list(@RequestBody PaginationReq req) {
        Page<ApplicationInfo> page = applicationInfoService.page(Page.of(req.getCurrent(), req.getPageSize()));
        return Result.success(page);
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody @Validated ApplicationCreateReqVo reqVo) {
        if (StringUtils.isBlank(reqVo.getAppKey())) {
            String appKey = IdUtil.getId();
            reqVo.setAppKey(appKey);
        }
        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.setAppId(IdUtil.getId());
        applicationInfo.setAppName(reqVo.getAppName());
        applicationInfo.setAppKey(
                StringUtils.isBlank(reqVo.getAppKey()) ? IdUtil.getId() : reqVo.getAppKey());
        applicationInfo.setAppSecret(
                StringUtils.isBlank(reqVo.getAppSecret()) ? IdUtil.getId() : reqVo.getAppSecret());
        applicationInfo.setEnterpriseId(reqVo.getEnterpriseId());
        applicationInfo.setCryptorEnable(reqVo.getCryptorEnable());
        applicationInfo.setSecretId(reqVo.getSecretId());
        applicationInfo.setRequestSecretId(reqVo.getRequestSecretId());
        applicationInfo.setResponseSecretId(reqVo.getResponseSecretId());
        applicationInfo.setCreateId("xx");
        applicationInfo.setCreateTime(LocalDateTime.now());
        applicationInfo.setUpdateId("xx");
        applicationInfo.setUpdateTime(LocalDateTime.now());
        applicationInfoService.save(applicationInfo);
        appEventPublisher.onCreated(applicationInfo);
        return Result.success();
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody @Validated ApplicationDeleteReqVo reqVo) {
        ApplicationInfo applicationInfo = applicationInfoService.getOne(
                new LambdaQueryWrapper<ApplicationInfo>()
                        .eq(ApplicationInfo::getAppId, reqVo.getAppId()));
        applicationInfoService.removeById(applicationInfo);
        appEventPublisher.onDeleted(applicationInfo);
        return Result.success();
    }

    @PostMapping("update")
    public Result<Object> update(@RequestBody @Validated ApplicationUpdateReqVo reqVo) {
        ApplicationInfo applicationInfo = applicationInfoService.getOne(
                new LambdaQueryWrapper<ApplicationInfo>()
                        .eq(ApplicationInfo::getAppId, reqVo.getAppId()));
        applicationInfo.setAppName(reqVo.getAppName());
        applicationInfo.setEnterpriseId(reqVo.getEnterpriseId());
        applicationInfo.setCryptorEnable(reqVo.getCryptorEnable());
        applicationInfo.setSecretId(reqVo.getSecretId());
        applicationInfo.setRequestSecretId(reqVo.getRequestSecretId());
        applicationInfo.setResponseSecretId(reqVo.getResponseSecretId());
        applicationInfo.setUpdateTime(LocalDateTime.now());
        applicationInfoService.updateById(applicationInfo);
        appEventPublisher.onUpdated(applicationInfo);
        return Result.success();
    }

    @PostMapping("detail")
    public Result<ApplicationInfo> detail(@RequestBody @Validated ApplicationDeleteReqVo reqVo) {
        return Result.success(applicationInfoService.getOne(
                new LambdaQueryWrapper<ApplicationInfo>()
                        .eq(ApplicationInfo::getAppId, reqVo.getAppId())));
    }
}
