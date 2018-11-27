package com.gupao.activemq.transaction;

import com.gupao.activemq.App;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSQueueConsumerFalse {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory
                ("tcp://"+ App.SERVER_IP +":61616");
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();

//            Session session = connection.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);   //自动确认
            Session session = connection.createSession(
                    Boolean.FALSE,Session.CLIENT_ACKNOWLEDGE);   //手动确认  message.acknowledge();
//            Session session = connection.createSession(Boolean.FALSE,Session.DUPS_OK_ACKNOWLEDGE); //延迟确认，可能收到重复消息

            Destination destination = session.createQueue("myQueue");
            //创建接收者
            MessageConsumer consumer = session.createConsumer(destination);

            for (int i = 0; i < 10; i++) {
                Message message = consumer.receive();
                if(message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage)message;
                    System.out.println("consumer receive data:"+textMessage.getText());
                }
                if(i == 7){
                    message.acknowledge();
                }
            }

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
