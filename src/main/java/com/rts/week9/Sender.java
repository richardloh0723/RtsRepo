package com.rts.week9;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		
		try(Connection connection = factory.newConnection()) {
			Channel channel = connection.createChannel();
			channel.queueDeclare("hello_world", false, false, false, null);
			String message = "this is a test of rabbit mq ";
			channel.basicPublish("", "hello_world", false, null, message.getBytes());
			System.out.println("message to HW1 sent");
		}
	}
}
