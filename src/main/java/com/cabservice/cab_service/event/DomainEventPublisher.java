package com.cabservice.cab_service.event;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}

