package com.serezk4.controller.ws.configuration;

import com.serezk4.controller.ws.listener.RabbitMQListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    public static final String QUEUE_NAME = "chat-queue";
    public static final String NOTIFICATION_EXCHANGE = "notificationExchange";

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(
            ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RabbitMQListener listener) {
        return new MessageListenerAdapter(listener, "receiveMessage");
    }

    @Bean
    public Exchange notificationExchange() {
        return ExchangeBuilder.topicExchange(NOTIFICATION_EXCHANGE).durable(true).build();
    }

    public static Queue createQueueForChat(Long chatId) {
        return QueueBuilder.durable("chat.notifications." + chatId).build();
    }

    public static Binding bindQueueToExchange(Queue queue) {
        return BindingBuilder.bind(queue).to(new TopicExchange(NOTIFICATION_EXCHANGE))
                .with("chat." + queue.getName());
    }
}
