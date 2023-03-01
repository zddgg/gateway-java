package com.gateway.admin.listener;

import com.gateway.admin.dto.cache.AppData;
import com.gateway.admin.dto.cache.RouteData;
import com.gateway.admin.dto.cache.SecretData;
import com.gateway.admin.dto.cache.UpstreamData;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class DataChangedEventDispatcher implements ApplicationListener<DataChangedEvent>, InitializingBean {

    private final ApplicationContext applicationContext;

    private List<DataChangedListener> listeners;

    public DataChangedEventDispatcher(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onApplicationEvent(@NotNull final DataChangedEvent event) {
        for (DataChangedListener listener : listeners) {
            switch (event.getGroupKey()) {
                case APP:
                    listener.onAppChanged((List<AppData>) event.getSource(), event.getEventType());
                    break;
                case ROUTE:
                    listener.onRouteChanged((List<RouteData>) event.getSource(), event.getEventType());
                    break;
                case UPSTREAM:
                    listener.onUpstreamChanged((List<UpstreamData>) event.getSource(), event.getEventType());
                    break;
                case SECRET:
                    listener.onSecretChanged((List<SecretData>) event.getSource(), event.getEventType());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + event.getGroupKey());
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        Collection<DataChangedListener> listenerBeans = applicationContext.getBeansOfType(DataChangedListener.class).values();
        this.listeners = Collections.unmodifiableList(new ArrayList<>(listenerBeans));
    }
}
