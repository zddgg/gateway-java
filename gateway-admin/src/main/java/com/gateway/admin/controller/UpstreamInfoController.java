package com.gateway.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gateway.admin.entity.UpstreamInfo;
import com.gateway.admin.service.UpstreamInfoService;
import com.gateway.admin.service.publish.UpstreamEventPublisher;
import com.gateway.admin.utils.IdUtil;
import com.gateway.admin.vo.UpstreamCreateReqVo;
import com.gateway.admin.vo.UpstreamDeleteReqVo;
import com.gateway.admin.vo.UpstreamUpdateReqVo;
import com.voidtime.mall.common.request.PaginationReq;
import com.voidtime.mall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("upstream")
public class UpstreamInfoController {

    private final UpstreamInfoService upstreamInfoService;

    private final UpstreamEventPublisher upstreamEventPublisher;

    public UpstreamInfoController(UpstreamInfoService upstreamInfoService,
                                  UpstreamEventPublisher upstreamEventPublisher) {
        this.upstreamInfoService = upstreamInfoService;
        this.upstreamEventPublisher = upstreamEventPublisher;
    }


    @PostMapping("list")
    public Result<Page<UpstreamInfo>> list(@RequestBody PaginationReq req) {
        Page<UpstreamInfo> page = upstreamInfoService.page(Page.of(req.getCurrent(), req.getPageSize()));
        return Result.success(page);
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody @Validated UpstreamCreateReqVo reqVo) {
        UpstreamInfo upstreamInfo = new UpstreamInfo();
        upstreamInfo.setUpstreamId(IdUtil.getId());
        upstreamInfo.setUpstreamName(reqVo.getUpstreamName());
        upstreamInfo.setUpstreamType(reqVo.getUpstreamType());
        upstreamInfo.setHttpAddress(reqVo.getHttpAddress());
        upstreamInfo.setDiscoveryType(reqVo.getDiscoveryType());
        upstreamInfo.setCreateId("xx");
        upstreamInfo.setCreateTime(LocalDateTime.now());
        upstreamInfo.setUpdateId("xx");
        upstreamInfo.setUpdateTime(LocalDateTime.now());
        upstreamInfoService.save(upstreamInfo);
        upstreamEventPublisher.onCreated(upstreamInfo);
        return Result.success();
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody @Validated UpstreamDeleteReqVo reqVo) {
        UpstreamInfo upstreamInfo = upstreamInfoService.getOne(
                new LambdaQueryWrapper<UpstreamInfo>()
                        .eq(UpstreamInfo::getUpstreamId, reqVo.getUpstreamId()));
        upstreamInfoService.remove(
                new LambdaQueryWrapper<UpstreamInfo>()
                        .eq(UpstreamInfo::getUpstreamId, reqVo.getUpstreamId()));
        upstreamEventPublisher.onDeleted(upstreamInfo);
        return Result.success();
    }

    @PostMapping("update")
    public Result<Object> update(@RequestBody @Validated UpstreamUpdateReqVo reqVo) {
        UpstreamInfo upstreamInfo = upstreamInfoService.getOne(
                new LambdaQueryWrapper<UpstreamInfo>()
                        .eq(UpstreamInfo::getUpstreamId, reqVo.getUpstreamId()));
        upstreamInfo.setUpstreamName(reqVo.getUpstreamName());
        upstreamInfo.setUpstreamType(reqVo.getUpstreamType());
        upstreamInfo.setHttpAddress(reqVo.getHttpAddress());
        upstreamInfo.setDiscoveryType(reqVo.getDiscoveryType());
        upstreamInfo.setUpdateTime(LocalDateTime.now());
        upstreamInfoService.updateById(upstreamInfo);
        upstreamEventPublisher.onUpdated(upstreamInfo);
        return Result.success();
    }

    @PostMapping("detail")
    public Result<UpstreamInfo> detail(@RequestBody @Validated UpstreamDeleteReqVo reqVo) {
        return Result.success(upstreamInfoService.getOne(
                new LambdaQueryWrapper<UpstreamInfo>()
                        .eq(UpstreamInfo::getUpstreamId, reqVo.getUpstreamId())));
    }
}
