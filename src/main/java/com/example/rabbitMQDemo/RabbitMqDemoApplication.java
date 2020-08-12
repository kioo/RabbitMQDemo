package com.example.rabbitMQDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitMqDemoApplication {

	static final String topicExchangeName = "spring-boot-exchange";
	static final String queueName = "spring-boot";

	// 创建 队列 bean
	@Bean
	Queue queue(){
		return new Queue(queueName, false);
	}
	// 创建 exchange Topic 类型的
	@Bean
	TopicExchange exchange(){
		return new TopicExchange(topicExchangeName);
	}
	// 将上面的 队列和exchange 绑定起来
	@Bean
	Binding binding(Queue queue, TopicExchange exchange){
		return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
	}

	// 传入 connectionFactory和 MessageListenerAdapter来配置SimpleMessageListenerContainer
	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
											 MessageListenerAdapter listenerAdapter){
		SimpleMessageListenerContainer container = new
				SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);

		return container;
	}

	// 配置消息接收者的适配器
	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver){
		return new MessageListenerAdapter(receiver,"receiveMessage");
	}

	public static void main(String[] args) {
		SpringApplication.run(RabbitMqDemoApplication.class, args).close();
	}

}
