package com.gateway.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gateway.admin.entity.RouteInfo;
import com.gateway.admin.service.RouteInfoService;
import com.gateway.admin.service.publish.RouteEventPublisher;
import com.gateway.admin.utils.IdUtil;
import com.gateway.admin.vo.RouteCreateReqVo;
import com.gateway.admin.vo.RouteDeleteReqVo;
import com.gateway.admin.vo.RouteUpdateReqVo;
import com.voidtime.mall.common.request.PaginationReq;
import com.voidtime.mall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("route")
public class RouteInfoController {

    private final RouteInfoService routeInfoService;

    private final RouteEventPublisher routeEventPublisher;

    public RouteInfoController(RouteInfoService routeInfoService,
                               RouteEventPublisher routeEventPublisher) {
        this.routeInfoService = routeInfoService;
        this.routeEventPublisher = routeEventPublisher;
    }


    @PostMapping("list")
    public Result<Page<RouteInfo>> list(@RequestBody PaginationReq req) {
        Page<RouteInfo> page = routeInfoService.page(Page.of(req.getCurrent(), req.getPageSize()));
        return Result.success(page);
    }


    @PostMapping("create")
    public Result<Object> create(@RequestBody @Validated RouteCreateReqVo reqVo) {
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setRouteId(IdUtil.getId());
        routeInfo.setRouteName(reqVo.getRouteName());
        routeInfo.setRoutePath(reqVo.getRoutePath());
        routeInfo.setRouteVersion(reqVo.getRouteVersion());
        routeInfo.setUpstreamId(reqVo.getUpstreamId());
        routeInfo.setCreateId("xx");
        routeInfo.setCreateTime(LocalDateTime.now());
        routeInfo.setUpdateId("xx");
        routeInfo.setUpdateTime(LocalDateTime.now());
        routeInfoService.save(routeInfo);
        routeEventPublisher.onCreated(routeInfo);
        return Result.success();
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody @Validated RouteDeleteReqVo reqVo) {
        RouteInfo routeInfo = routeInfoService.getOne(
                new LambdaQueryWrapper<RouteInfo>()
                        .eq(RouteInfo::getRouteId, reqVo.getRouteId()));
        routeInfoService.removeById(routeInfo);
        routeEventPublisher.onDeleted(routeInfo);
        return Result.success();
    }

    @PostMapping("update")
    public Result<Object> update(@RequestBody @Validated RouteUpdateReqVo reqVo) {
        RouteInfo routeInfo = routeInfoService.getOne(
                new LambdaQueryWrapper<RouteInfo>()
                        .eq(RouteInfo::getRouteId, reqVo.getRouteId()));
        routeInfo.setRouteName(reqVo.getRouteName());
        routeInfo.setRoutePath(reqVo.getRoutePath());
        routeInfo.setRouteVersion(reqVo.getRouteVersion());
        routeInfo.setUpstreamId(reqVo.getUpstreamId());
        routeInfo.setUpdateTime(LocalDateTime.now());
        routeInfoService.updateById(routeInfo);
        routeEventPublisher.onUpdated(routeInfo);
        return Result.success();
    }

    @PostMapping("detail")
    public Result<RouteInfo> detail(@RequestBody @Validated RouteDeleteReqVo reqVo) {
        return Result.success(routeInfoService.getOne(
                new LambdaQueryWrapper<RouteInfo>()
                        .eq(RouteInfo::getRouteId, reqVo.getRouteId())));
    }
}
