package com.nanomt88.demo.rocketmq.translation;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * Created by ZBOOK-17 on 2017/6/3.
 */
public class Consumer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ProducerGroupName");

        consumer.setNamesrvAddr("192.168.1.140:9876;192.168.1.141:9876");


        /**
         * 设置consumer启动后是从头部开始消费还是从队列尾部开始消费
         * 如果非第一次启动，需要按照上一次消费的位置进行消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        /**
         * 设置 在Push模式下，一次最多拉取的消息数量
         */
        consumer.setConsumeMessageBatchMaxSize(1);

        consumer.setConsumeConcurrentlyMaxSpan(1000);

        consumer.subscribe("TransactionTopic", "*");
        //consumer.subscribe("TopicTest2", "*");
        //consumer.subscribe("TopicTest3", "*");

        //注册从broker读取到消息之后，处理消息的回调事件
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                /**
                 *  List<MessageExt> msgs : 参数msgs为list类型，可以进行批量拉取消息
                 *
                 */
                try {
                    /**
                     *  这里处理业务逻辑，业务逻辑处理成功之后才返回 CONSUME_SUCCESS
                     */
                    for(MessageExt msg :  msgs) {
                        System.out.printf(Thread.currentThread().getName() + " Receive New Messages, id: "+msg.getKeys() + "  - message : " + msg + "%n");
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    /**
                     * 如果消息出现异常，就返回失败，下次会重新消费这一条消息
                     */
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        /*
         *  启动
         */
        consumer.start();

        System.out.printf("Consumer Started.%n");
    }
}
