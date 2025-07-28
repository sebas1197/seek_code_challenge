package com.seek.infrastructure.messaging;

import com.seek.domain.event.CustomerCreatedEvent;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerEventProducer {

    private final PubSubTemplate pubSubTemplate;

    public void send(CustomerCreatedEvent evt) {
        pubSubTemplate.publish("customer.created", evt);
    }
}
