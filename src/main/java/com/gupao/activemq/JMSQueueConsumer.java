package com.gupao.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSQueueConsumer {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory
                ("tcp://"+ App.SERVER_IP +":61616");
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue("myQueue");
            //创建接收者
            MessageConsumer consumer = session.createConsumer(destination);

            Message message = consumer.receive();

            if(message instanceof TextMessage){
                TextMessage textMessage = (TextMessage)message;
                System.out.println("consumer receive data:"+textMessage.getText());
            }

            session.commit();
//            session.rollback();
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
