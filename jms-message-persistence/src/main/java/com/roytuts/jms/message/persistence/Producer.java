package com.roytuts.jms.message.persistence;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Producer {

    public static void main(String[] args) {
        new Producer().produceMessage();
    }

    public void produceMessage() {
        InitialContext initialContext = null;
        QueueConnectionFactory connectionFactory;
        QueueConnection connection = null;
        QueueSender sender;
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
            sender = session.createSender(queue);

            // step 8. non-persistence delivery of message
            //sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Step 9. Create a text message
            TextMessage message = session.createTextMessage("This sample message is consumed by consumer");

            // Step 10. Send the text message to the queue
            sender.send(message);
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
