package com.nanomt88.demo.rocketmq.order;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.Date;
import java.util.List;

/**
 *  顺序消费场景 demo
 */
public class Producer {

    public static void main(String[] args) throws MQClientException {
        /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ProducerGroupName需要由应用来保证唯一<br>
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         */
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");

        producer.setNamesrvAddr("192.168.1.140:9876;192.168.1.141:9876");
        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         * 注意：切记不可以在每次发送消息时，都调用start方法
         */
        producer.start();

        /**
         * 下面这段代码表明一个Producer对象可以发送多个topic，多个tag的消息。
         * 注意：send方法是同步调用，只要不抛异常就标识成功。但是发送成功也可会有多种状态，<br>
         * 例如消息写入Master成功，但是Slave不成功，这种情况消息属于成功，但是对于个别应用如果对消息可靠性要求极高，<br>
         * 需要对这种情况做处理。另外，消息可能会存在发送失败的情况，失败重试由应用来处理。
         */
        for (int i = 0; i < 4; i++){
            try {
                {
                    Message msg = new Message("TopicTest1",// topic
                            "order_1",// tag
                            "OrderID-A00"+i,// key ： 用于唯一标记，方便去重
                            (new Date().toString() + "message_A"+i).getBytes());// body
                    //发送数据： 如果使用顺序消费，必须直接自己实现MessageQueueSelector接口，保证消息发送到同一个队列
                    SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                        @Override
                        public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                            // 参数arg是外层传入的0，用于选择队列
                            Integer index = (Integer) arg;
                            return mqs.get(index);
                        }
                    },0 );  //0 : 表示队列的下标
                    System.out.println(sendResult);
                }

                {
                    Message msg = new Message("TopicTest1",// topic
                            "order_2",// tag
                            "OrderID-B00"+i,// key
                            (new Date().toString() + "message_B"+i).getBytes());// body
                    SendResult sendResult = producer.send(msg);
                    System.out.println(sendResult);
                }

                {
                    Message msg = new Message("TopicTest1",// topic
                            "order_3",// tag
                            "OrderID-C00"+i,// key
                            (new Date().toString() + "message_C"+i).getBytes());// body
                    SendResult sendResult = producer.send(msg);
                    System.out.println(sendResult);
                }

                {
                    Message msg = new Message("TopicTest1",// topic
                            "order_4",// tag
                            "OrderID-D00"+i,// key
                            (new Date().toString() + "message_D"+i).getBytes());// body
                    SendResult sendResult = producer.send(msg);
                    System.out.println(sendResult);
                }
            } catch (RemotingException e) {
                e.printStackTrace();
            } catch (MQBrokerException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        /**
         * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己
         * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
         */
        producer.shutdown();

    }
}
