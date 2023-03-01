package com.gateway.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gateway.admin.entity.SecretInfo;
import com.gateway.admin.service.SecretInfoService;
import com.gateway.admin.service.publish.SecretEventPublisher;
import com.gateway.admin.utils.IdUtil;
import com.gateway.admin.vo.SecretCreateReqVo;
import com.gateway.admin.vo.SecretDeleteReqVo;
import com.gateway.admin.vo.SecretUpdateReqVo;
import com.voidtime.mall.common.request.PaginationReq;
import com.voidtime.mall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("secret")
public class SecretInfoController {

    private final SecretInfoService secretInfoService;

    private final SecretEventPublisher secretEventPublisher;

    public SecretInfoController(SecretInfoService secretInfoService,
                                SecretEventPublisher secretEventPublisher) {
        this.secretInfoService = secretInfoService;
        this.secretEventPublisher = secretEventPublisher;
    }

    @PostMapping("list")
    public Result<Page<SecretInfo>> list(@RequestBody PaginationReq req) {
        Page<SecretInfo> page = secretInfoService.page(Page.of(req.getCurrent(), req.getPageSize()));
        return Result.success(page);
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody @Validated SecretCreateReqVo reqVo) {
        SecretInfo secretInfo = new SecretInfo();
        secretInfo.setSecretId(IdUtil.getId());
        secretInfo.setSecretName(reqVo.getSecretName());
        secretInfo.setSecretType(reqVo.getSecretType());
        secretInfo.setSecretKey(reqVo.getSecretKey());
        secretInfo.setPublicKey(reqVo.getPublicKey());
        secretInfo.setPrivateKey(reqVo.getPrivateKey());
        secretInfo.setEncodingType(reqVo.getEncodingType());
        secretInfo.setCreateId("xx");
        secretInfo.setCreateTime(LocalDateTime.now());
        secretInfo.setUpdateId("xx");
        secretInfo.setUpdateTime(LocalDateTime.now());
        secretInfoService.save(secretInfo);
        secretEventPublisher.onCreated(secretInfo);
        return Result.success();
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody @Validated SecretDeleteReqVo reqVo) {
        SecretInfo secretInfo = secretInfoService.getOne(
                new LambdaQueryWrapper<SecretInfo>()
                        .eq(SecretInfo::getSecretId, reqVo.getSecretId()));
        secretInfoService.removeById(secretInfo);
        secretEventPublisher.onDeleted(secretInfo);
        return Result.success();
    }

    @PostMapping("update")
    public Result<Object> update(@RequestBody @Validated SecretUpdateReqVo reqVo) {
        SecretInfo secretInfo = secretInfoService.getOne(
                new LambdaQueryWrapper<SecretInfo>()
                        .eq(SecretInfo::getSecretId, reqVo.getSecretId()));
        secretInfo.setSecretName(reqVo.getSecretName());
        secretInfo.setSecretType(reqVo.getSecretType());
        secretInfo.setSecretKey(reqVo.getSecretKey());
        secretInfo.setPublicKey(reqVo.getPublicKey());
        secretInfo.setPrivateKey(reqVo.getPrivateKey());
        secretInfo.setEncodingType(reqVo.getEncodingType());
        secretInfo.setUpdateTime(LocalDateTime.now());
        secretInfoService.updateById(secretInfo);
        secretEventPublisher.onUpdated(secretInfo);
        return Result.success();
    }

    @PostMapping("detail")
    public Result<SecretInfo> detail(@RequestBody @Validated SecretDeleteReqVo reqVo) {
        return Result.success(secretInfoService.getOne(
                new LambdaQueryWrapper<SecretInfo>()
                        .eq(SecretInfo::getSecretId, reqVo.getSecretId())));
    }
}
