package com.roytuts.jms.message.persistence;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Consumer {

    public static void main(String[] args) {
        new Consumer().consumeMessage();
    }

    public void consumeMessage() {
        InitialContext initialContext = null;
        QueueConnectionFactory connectionFactory;
        QueueConnection connection = null;
        MessageConsumer consumer;
        QueueSession session;
        Queue queue;

        try {
            // Step 1. Create an initial context to perform the JNDI lookup.
            initialContext = new InitialContext();

            // Step 2. Look-up the JMS queue
            queue = (Queue) initialContext.lookup("queue/queueName");

            // Step 3. Look-up the JMS queue connection factory
            connectionFactory = (QueueConnectionFactory) initialContext.lookup("ConnectionFactory");

            // Step 4. Create a JMS queue connection
            connection = connectionFactory.createQueueConnection();

            // Step 5. Set the client-id on the connection
            connection.start();

            // step 6. Create queue session
            session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);

            // step 7. Create queue sender
            consumer = session.createConsumer(queue);

            // Step 8. receive text message
            Message message = consumer.receive();
            if (message != null && message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                System.out.println("Consumer received a message produced by Producer : " + textMessage.getText());
            } else if (message == null) {
                System.out.println("Consumer fails to receive the message sent by the producer.");
            } else {
                throw new JMSException("Message must be a type of TextMessage");
            }
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
