package com.gateway.admin.service.impl;

import com.gateway.admin.dto.ConfigGroupEnum;
import com.gateway.admin.dto.cache.AppData;
import com.gateway.admin.dto.cache.RouteData;
import com.gateway.admin.dto.cache.SecretData;
import com.gateway.admin.dto.cache.UpstreamData;
import com.gateway.admin.enums.DataEventTypeEnum;
import com.gateway.admin.listener.DataChangedEvent;
import com.gateway.admin.service.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SyncDataServiceImpl implements SyncDataService {

    private final ApplicationEventPublisher eventPublisher;

    private final ApplicationInfoService applicationInfoService;

    private final RouteInfoService routeInfoService;

    private final UpstreamInfoService upstreamInfoService;

    private final SecretInfoService secretInfoService;

    public SyncDataServiceImpl(ApplicationEventPublisher eventPublisher,
                               ApplicationInfoService applicationInfoService,
                               RouteInfoService routeInfoService,
                               UpstreamInfoService upstreamInfoService,
                               SecretInfoService secretInfoService) {
        this.eventPublisher = eventPublisher;
        this.applicationInfoService = applicationInfoService;
        this.routeInfoService = routeInfoService;
        this.upstreamInfoService = upstreamInfoService;
        this.secretInfoService = secretInfoService;
    }

    @Override
    public boolean syncAll(DataEventTypeEnum type) {
        eventPublisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.APP, type, prepareAppData()));
        eventPublisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.ROUTE, type, prepareRouteData()));
        eventPublisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.UPSTREAM, type, prepareUpstreamData()));
        eventPublisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.SECRET, type, prepareSecretData()));
        return true;
    }

    @Override
    public boolean syncPluginData(String pluginId) {
        return false;
    }

    private List<AppData> prepareAppData() {
        return applicationInfoService.list()
                .stream()
                .map(AppData::convert)
                .collect(Collectors.toList());
    }

    private List<RouteData> prepareRouteData() {
        return routeInfoService.list()
                .stream()
                .map(RouteData::convert)
                .collect(Collectors.toList());
    }

    private List<UpstreamData> prepareUpstreamData() {
        return upstreamInfoService.list()
                .stream()
                .map(UpstreamData::convert)
                .collect(Collectors.toList());
    }

    private List<SecretData> prepareSecretData() {
        return secretInfoService.list()
                .stream()
                .map(SecretData::convert)
                .collect(Collectors.toList());
    }
}
