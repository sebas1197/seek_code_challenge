package com.seek.infrastructure.messaging;

import com.seek.domain.event.CustomerCreatedEvent;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventListener {

    private final MeterRegistry meterRegistry;
    private final PubSubTemplate pubSubTemplate;
    private Counter counter;

    private Counter counter() {
        if (counter == null) {
            counter = Counter.builder("customer_created_total")
                    .description("Total number of clients processed")
                    .register(meterRegistry);
        }
        return counter;
    }

    @PostConstruct
    public void init() {
        pubSubTemplate
                .subscribe("customer-created-subscription",
                        message -> {
                            CustomerCreatedEvent event = message.getPayload();
                            log.info("Processed customer {} via Pub/Sub", event.getId());
                            counter().increment();
                            message.ack();
                        });
    }
}
