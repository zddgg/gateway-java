package com.gateway.admin.listener;

import com.gateway.admin.dto.ConfigGroupEnum;
import com.gateway.admin.enums.DataEventTypeEnum;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Data change event.
 *
 * @see DataChangedEventDispatcher
 * @since 2.0.0
 */
public class DataChangedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 8397163004365988739L;

    private final DataEventTypeEnum eventType;

    private final ConfigGroupEnum groupKey;

    /**
     * Instantiates a new Data changed event.
     *
     * @param groupKey the group key
     * @param type     the type
     * @param source   the source
     */
    public DataChangedEvent(final ConfigGroupEnum groupKey, final DataEventTypeEnum type, final List<?> source) {
        super(source.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        this.eventType = type;
        this.groupKey = groupKey;
    }

    /**
     * Gets event type.
     *
     * @return the event type
     */
    DataEventTypeEnum getEventType() {
        return eventType;
    }

    @Override
    public List<?> getSource() {
        return (List<?>) super.getSource();
    }

    /**
     * Gets group key.
     *
     * @return the group key
     */
    public ConfigGroupEnum getGroupKey() {
        return this.groupKey;
    }

}
