package com.cabservice.cab_service.event;


public interface DomainEventHandler<E extends DomainEvent> {

    String getSupportedEventName();
    void handle(E event);
}

