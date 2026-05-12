package com.example.user_service.cfg;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@EnableRabbit
@Configuration
public class RabbitConfig {
    @Bean
    public Queue passengerExistsQueue() {
        return new Queue("user.passenger.exists.queue");
    }

    @Bean
    public Queue findFreeDriverQueue() {
        return new Queue("user.driver.find.queue");
    }

    @Bean
    public Queue updateDriverStatusQueue() {
        return new Queue("user.driver.status.update.queue");
    }

    @Bean
    public TopicExchange tripExchange() {
        return new TopicExchange("trip.exchange");
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange("user.exchange");
    }

    @Bean
    public Binding bindPassengerExistsQueue(Queue passengerExistsQueue, TopicExchange userExchange) {
        return BindingBuilder
                .bind(passengerExistsQueue)
                .to(userExchange)
                .with("user.passenger.exists");
    }

    @Bean
    public Binding bindFindFreeDriverQueue(Queue findFreeDriverQueue, TopicExchange userExchange) {
        return BindingBuilder
                .bind(findFreeDriverQueue)
                .to(userExchange)
                .with("user.driver.find");
    }

    @Bean
    public Binding bindUpdateDriverStatusQueue(Queue updateDriverStatusQueue, TopicExchange userExchange) {
        return BindingBuilder
                .bind(updateDriverStatusQueue)
                .to(userExchange)
                .with("user.driver.status.update");
    }

    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("*");
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());

        return factory;
    }
}