package com.cabservice.cab_service.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class AnalyticsEventListener {

    private final Map<String, DomainEventHandler<? extends DomainEvent>> handlersByName = new HashMap<>();

    public AnalyticsEventListener(List<DomainEventHandler<? extends DomainEvent>> handlers) {
        for (DomainEventHandler<? extends DomainEvent> handler : handlers) {
            handlersByName.put(handler.getSupportedEventName(), handler);
        }
    }

    @EventListener
    public void onDomainEvent(DomainEvent event) {
        DomainEventHandler<? extends DomainEvent> handler = handlersByName.get(event.getEventName());
        if (handler == null) {
            return;
        }
        dispatch(handler, event);
    }

    @SuppressWarnings("unchecked")
    private <E extends DomainEvent> void dispatch(DomainEventHandler<? extends DomainEvent> rawHandler,
                                                 DomainEvent event) {
        DomainEventHandler<E> handler = (DomainEventHandler<E>) rawHandler;
        handler.handle((E) event);
    }
}

