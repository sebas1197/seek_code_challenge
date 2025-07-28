package com.seek.infrastructure.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declaración de cola, intercambio y binding para eventos de cliente.
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE  = "customer.exchange";
    public static final String QUEUE     = "customer.created.queue";
    public static final String ROUTING_KEY = "customer.created";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                             .to(exchange)
                             .with(ROUTING_KEY);
    }
}
