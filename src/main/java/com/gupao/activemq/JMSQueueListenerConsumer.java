package com.gupao.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSQueueListenerConsumer {

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

//            MessageListener messageListener = new MessageListener() {
//                @Override
//                public void onMessage(Message message) {
//                    if(message instanceof TextMessage){
//                        TextMessage textMessage = (TextMessage)message;
//                        try {
//                            System.out.println("consumer receive data :"+textMessage.getText());
//                        } catch (JMSException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            };

            MessageListener messageListener = (message)->{
                if(message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage)message;
                    try {
                        System.out.println("consumer receive data :"+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            };

            while (true){
                consumer.setMessageListener(messageListener);
                session.commit();
            }
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
