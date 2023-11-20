package com.roytuts.jms.message.priority.consumer;

import java.time.LocalTime;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

	Session session;
	Connection connection;
	MessageConsumer consumer;
	InitialContext initialContext;

	public void initialContext() {
		try {
			initialContext = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public void openConnection() {
		try {
			ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
			connection = connectionFactory.createConnection();
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void createConsumer() {
		try {
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination queue = (Queue) initialContext.lookup("queue/queueName");

			consumer = session.createConsumer(queue);

			connection.start();
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}
	}

	public void receive() {
		Message message;
		try {
			message = consumer.receive();
			if (message != null && message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				LOGGER.info("Consumer received a message '{}' at {}", textMessage.getText(), LocalTime.now());
			} else if (message == null) {
				LOGGER.error("Consumer fails to receive the message sent by the producer.");
			} else {
				throw new JMSException("Message must be a type of TextMessage");
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
