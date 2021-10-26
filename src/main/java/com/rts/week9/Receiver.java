package com.rts.week9;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Receiver {
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		
		Connection connection = factory.newConnection();
		
		Channel channel = connection.createChannel();
		
		channel.queueDeclare("hello_world", false, false, false, null);
		
		channel.basicConsume("hello_world", true, (consumerTag, message)->{
			String m = new String(message.getBody(), "UTF-8");
			System.out.println("Message received R1: " + m);
			long end = System.currentTimeMillis();
		}, consumerTag -> {});
	}
}
