package com.cabservice.cab_service;

/**
 * Generic handler for domain events, identified by a logical event name.
 */
public interface DomainEventHandler<E extends DomainEvent> {

    /**
     * Name of the event this handler supports.
     */
    String getSupportedEventName();

    /**
     * Handle the given domain event.
     */
    void handle(E event);
}

