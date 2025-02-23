package com.gomo.eventconsumer.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

	@Value("${spring.rabbitmq.exchange.quest}")
	private String questExchange;

	@Value("${spring.rabbitmq.queue.quest.completed.score}")
	private String questCompletedScoreQueue;

	@Value("${spring.rabbitmq.queue.quest.completed.point}")
	private String questCompletedPointQueue;

	@Value("${spring.rabbitmq.queue.quest.completed.streak}")
	private String questCompletedStreakQueue;

	@Value("${spring.rabbitmq.routing-key.quest.completed.score}")
	private String questCompletedScoreRoutingKey;

	@Value("${spring.rabbitmq.routing-key.quest.completed.point}")
	private String questCompletedPointRoutingKey;

	@Value("${spring.rabbitmq.routing-key.quest.completed.streak}")
	private String questCompletedStreakRoutingKey;

	@Bean
	public DirectExchange questExchange() {
		return new DirectExchange(questExchange, true, false);
	}

	@Bean
	public Queue questCompletedScoreQueue() {
		return new Queue(questCompletedScoreQueue, true);
	}

	@Bean
	public Queue questCompletedPointQueue() {
		return new Queue(questCompletedPointQueue, true);
	}

	@Bean
	public Queue questCompletedStreakQueue() {
		return new Queue(questCompletedStreakQueue, true);
	}

	@Bean
	public Binding questCompletedScoreBinding() {
		return BindingBuilder.bind(questCompletedScoreQueue()).to(questExchange()).with(questCompletedScoreRoutingKey);
	}

	@Bean
	public Binding questCompletedPointBinding() {
		return BindingBuilder.bind(questCompletedPointQueue()).to(questExchange()).with(questCompletedPointRoutingKey);
	}

	@Bean
	public Binding questCompletedStreakBinding() {
		return BindingBuilder.bind(questCompletedStreakQueue()).to(questExchange()).with(questCompletedStreakRoutingKey);
	}
}
