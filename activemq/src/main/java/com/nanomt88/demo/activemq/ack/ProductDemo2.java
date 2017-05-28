package com.nanomt88.demo.activemq.ack;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;

import javax.jms.*;

/**
 *  不同的签收模式
 *  支持事物
 */
public class ProductDemo2 {

    //private  static final Logger logger = LoggerFactory.getLogger(ProductDemo.class);

    public static void main(String[] args) throws JMSException {

        //1. 建立ConnectionFactory工厂对象，填入用户名、密码、已经要连接的地址
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                ActiveMQConnectionFactory.DEFAULT_BROKER_BIND_URL);

        //2. 通过ConnectionFacory对象创建一个Connection连接，并且调用start方法开启连接，Connection默认是关闭状态
        Connection connection = connectionFactory.createConnection();
        connection.start();

        //3. 通过Connection连接创建Session会话（上下文环境对象），用于接受消息和发送消息。
        // 第一个参数：true 启用事物； 第二个参数：签收模式，一般设置为自动签收
        Session session = connection.createSession(true, ActiveMQSession.CLIENT_ACKNOWLEDGE);

        //4. 通过session 创建 Destination对象，作用：一个客户端用来指定生产消息目标和消费者消息来源的对象。
        //PTP模式中，destination被称之为 队列即 Queue
        //在Pub/sub模式中，Destination被称为Topic即主题。程序中可以指定多个Queue和Topic
        Destination destination = session.createQueue("queue1");

        //5. 通过session创建消息的发送和接受对象（生产者和消费者）： MessageProduct、 MessageConsumer
        MessageProducer producer = session.createProducer(null);

        //6. 可以使用 MessageProducer的setDeliveryMode 方法设置消息是否需要持久化
        //producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        //7. 最后使用JMS规范的TextMessage形式创建数据对象，并用MessageProduct对象send方法发送数据。
        //     消费端使用receive方法接受数据。最后要记得关闭connection
        for (int i = 0; i < 5; i++) {
            TextMessage message = session.createTextMessage();
            message.setText("发送消息啦啦啦"+ i);

            producer.send(destination,message, DeliveryMode.NON_PERSISTENT, i, 1000*60*60);
            //logger.info("发送消息:{}", message);
            System.out.println("发送消息：" + i);
        }
        //使用事物提交的时候，需要手动commit
        session.commit();
        if(session != null) {
            connection.close();
        }
    }
}
