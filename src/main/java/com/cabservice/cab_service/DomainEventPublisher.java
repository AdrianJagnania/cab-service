package com.cabservice.cab_service;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}

