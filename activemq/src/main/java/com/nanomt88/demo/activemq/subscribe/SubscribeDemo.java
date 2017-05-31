package com.nanomt88.demo.activemq.subscribe;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;

import javax.jms.*;
import java.util.Date;

/**
 * activemq 发布订阅的demo
 */
public class SubscribeDemo {

    public static void main(String[] args) throws JMSException, InterruptedException {

        //1. 建立 TopicConnectionFactory 工厂对象，填入用户名、密码、已经要连接的地址
        TopicConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "failover:(tcp://192.168.1.130:61611,tcp://192.168.1.130:61612,tcp://192.168.1.130:61613)?Randomize=false");

        //2. 通过 TopicConnectionFactory 对象创建一个 TopicConnection 连接，并且调用start方法开启连接，Connection默认是关闭状态
        TopicConnection connection = connectionFactory.createTopicConnection();
        connection.start();

        //3. 通过connection连接创建TopicSession会话（上下文环境对象），用于订阅消息和发布消息。
        // 第一个参数：true 启用事物； 第二个参数：签收模式，一般设置为自动签收
        TopicSession session = connection.createTopicSession(false, ActiveMQSession.AUTO_ACKNOWLEDGE);

        //4. 通过 TopicSession 创建 Topic 对象，作用：一个客户端用来指定生产消息目标和消费者消息来源的对象。
        //PTP模式中，destination被称之为 队列即 Queue
        //在Pub/sub模式中，Destination被称为Topic即主题。程序中可以指定多个Queue和Topic
        Topic topic = session.createTopic("topic1");

        //5. 通过session创建消息的发送和接受对象（生产者和消费者）： TopicPublisher、 TopicSubscriber
        TopicSubscriber subscriber = session.createSubscriber(topic);


        //7. 最后使用JMS规范的TextMessage形式创建数据对象，并用MessageProduct对象send方法发送数据。
        //     消费端使用receive方法接受数据。最后要记得关闭connection

        subscriber.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                if(message !=null ){
                    TextMessage msg = (TextMessage) message;
                    try {
                        System.out.println(new Date().toString() + " : " + msg.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread.sleep(1000*60*5);

        if(session != null) {
            connection.close();
        }

    }
}
