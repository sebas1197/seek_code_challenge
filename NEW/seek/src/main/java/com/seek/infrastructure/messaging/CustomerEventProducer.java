package com.seek.infrastructure.messaging;

import com.seek.domain.event.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publica eventos en RabbitMQ después de persistir clientes.
 */
@Component
@RequiredArgsConstructor
public class CustomerEventProducer {

    private final PubSubTemplate pubSubTemplate;

    public void send(CustomerCreatedEvent evt) {
        pubSubTemplate.publish("customer.created", evt);
    }
}
