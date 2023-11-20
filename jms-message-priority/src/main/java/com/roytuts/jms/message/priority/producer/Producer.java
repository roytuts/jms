package com.roytuts.jms.message.priority.producer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Producer {

	public void produceMessage() {
		InitialContext initialContext = null;
		ConnectionFactory connectionFactory;
		Connection connection = null;
		MessageProducer sender;
		Session session;
		Destination queue;

		try {
			initialContext = new InitialContext();

			queue = (Queue) initialContext.lookup("queue/queueName");

			connectionFactory = (QueueConnectionFactory) initialContext.lookup("ConnectionFactory");

			connection = connectionFactory.createConnection();

			connection.start();

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			sender = session.createProducer(queue);

			sender.setPriority(3);

			// Send message with priority 3
			TextMessage message = session.createTextMessage("This message has a priority of 3");
			sender.send(message);

			// Send another message with priority 9
			message = session.createTextMessage("This message has a priority of 9");
			sender.send(message, DeliveryMode.PERSISTENT, 9, 0);
		} catch (JMSException | NamingException ex) {
			ex.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}

			if (initialContext != null) {
				try {
					initialContext.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
