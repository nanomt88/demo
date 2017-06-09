package com.nanomt88.demo.rocketmq.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * 顺序消费场景 demo
 */
public class Consumer {

    private  static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) throws MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ProducerGroupName");

        consumer.setNamesrvAddr("192.168.1.140:9876;192.168.1.141:9876");



        /**
         * 设置 在Push模式下，一次最多拉取的消息数量
         */
        consumer.setConsumeMessageBatchMaxSize(1);

        /**
         *
         *  setPullBatchSize 方法在 DefaultMQPullConsumer 方式下才会生效
         *  consumer.setPullBatchSize(32);
         */
        consumer.setPullInterval(0);

        // ============================== consumer 配置参数结束 =====================================


        consumer.subscribe("TopicTest1", "*");
        //consumer.subscribe("TopicTest2", "*");
        //consumer.subscribe("TopicTest3", "*");

        //注册从broker读取到消息之后，处理消息的回调事件
        //顺序消费必须实现：MessageListenerOrderly接口，这个接口保证单线程消费
        consumer.registerMessageListener(new MessageListenerOrderly() {

            Random random = new Random();
            @Override
        public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                /**
                 *  List<MessageExt> msgs : 参数msgs为list类型，可以进行批量拉取消息
                 *
                 */
                try {
                    /**
                     *  这里处理业务逻辑，业务逻辑处理成功之后才返回 CONSUME_SUCCESS
                     */
                    if(msgs!=null && msgs.size()>0){

                        MessageExt msg = msgs.get(0);
                        String body = new String(msg.getBody(),"UTF-8");
                        logger.info("{} 收到消息：tag:{}msg:{}" , new Object[] { body ,msg.getTags(),  msg});

                        Thread.sleep(random.nextInt(1000));
                        logger.info("{} 执行完毕", body);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    /**
                     * 如果消息出现异常，就返回失败，下次会重新消费这一条消息
                     */
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        /*
         *  启动
         */
        consumer.start();

        System.out.printf("Consumer Started.%n");
    }
}
