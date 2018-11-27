package com.gupao.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSTopicConsumer02 {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory
                ("tcp://"+ App.SERVER_IP +":61616");
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createTopic("myTopic");
            //创建接收者
            MessageConsumer consumer = session.createConsumer(destination);

            Message message = consumer.receive();

            if(message instanceof TextMessage){
                TextMessage textMessage = (TextMessage)message;
                System.out.println("consumer receive data 02:"+textMessage.getText());
            }

            session.commit();
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
