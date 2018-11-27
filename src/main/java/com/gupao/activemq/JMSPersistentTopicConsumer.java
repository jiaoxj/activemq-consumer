package com.gupao.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Enumeration;

/***
 * 持久化订阅
 *  需要先运行一次，将client_id注册到broker中，后边的才能订阅
 *
 */
public class JMSPersistentTopicConsumer {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory
                ("tcp://"+App.SERVER_IP +":61616");
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.setClientID("test-0001");
            connection.start();

            Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);

            Topic destination = session.createTopic("myTopic");
            //创建接收者
            MessageConsumer consumer = session.createDurableSubscriber(destination,"test-0001");

            Message message = consumer.receive();

            Enumeration enumeration = message.getPropertyNames();
            while(enumeration.hasMoreElements()){
                String name=enumeration.nextElement().toString();
                System.out.println("name:"+name+":"+message.getStringProperty(name));
            }

            if(message instanceof TextMessage){
                TextMessage textMessage = (TextMessage)message;
                System.out.println("Persistent consumer receive data 01:"+textMessage.getText());
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
