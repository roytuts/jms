package com.roytuts.jms.message.priority;

import com.roytuts.jms.message.priority.consumer.Consumer;
import com.roytuts.jms.message.priority.producer.Producer;

public class App {

	public static void main(String[] args) {
		Producer producer = new Producer();
		producer.produceMessage();

		Consumer consumer = new Consumer();

		consumer.initialContext();
		consumer.openConnection();

		consumer.createConsumer();

		consumer.receive();
		consumer.receive();

		consumer.closeConnection();
	}

}
