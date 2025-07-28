package com.seek.infrastructure.messaging;

import com.seek.domain.event.CustomerCreatedEvent;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor asíncrono de eventos de cliente.
 * Incrementa una métrica y escribe en log.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventListener {

    private final MeterRegistry meterRegistry;
    private Counter counter;

    /** Se inicializa el contador una sola vez */
    private Counter counter() {
        if (counter == null) {
            counter = Counter
                    .builder("customer_created_total")
                    .description("Número total de clientes procesados")
                    .register(meterRegistry);
        }
        return counter;
    }

    @PubSubSubscriber("customer.created")
    public void handle(CustomerCreatedEvent event) {
        log.info("Processed customer {} via Pub/Sub", event.getId());
        counter().increment();
    }

}
