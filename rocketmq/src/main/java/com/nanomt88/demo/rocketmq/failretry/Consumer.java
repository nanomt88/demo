package com.nanomt88.demo.rocketmq.failretry;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by ZBOOK-17 on 2017/6/3.
 */
public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ProducerGroupName");

        consumer.setNamesrvAddr("192.168.1.140:9876;192.168.1.141:9876");

        // ============================== consumer 配置参数开始 =====================================
        /**
         * 设置consumer启动后是从头部开始消费还是从队列尾部开始消费
         * 如果非第一次启动，需要按照上一次消费的位置进行消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        /**
         *  设置 消费线程池最小数量
         *  consumer.setConsumeThreadMin(5);
         *
         *  设置 消费线程池最大数量
         *  consumer.setConsumeThreadMax(20);
         */

        /**
         *  拉取消息本地队列缓存消息最大数量： 默认1000
         *  consumer.setPullThresholdForQueue(1000);
         */

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
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                /**
                 *  List<MessageExt> msgs : 参数msgs为list类型，可以进行批量拉取消息
                 *
                 */
                MessageExt msg = null;
                try {

                    logger.info("共收到消息：{}", msgs.size());

                    msg = msgs.get(0);
                    /**
                     *  这里处理业务逻辑，业务逻辑处理成功之后才返回 CONSUME_SUCCESS
                     */

                    logger.info("{} - Receive New Messages: {}", new Date().toString() +" - "+ Thread.currentThread().getName(), msg);

                    /**
                     * 模拟 其中一条处理失败之后，RocketMQ 会进行消息重试的 示例
                     */
                    String msgBody = new String(msg.getBody(), "UTF-8");
                    if("Hello mq 4".equals(msgBody)){
                        logger.info("===============消息：{}  处理失败================", msgBody);
                        /**
                         * 此处跑出异常之后，RocketMQ会返回 RECONSUME_LATER，然后进行消息重试
                         *
                         * 消息重试时，消息体内容：
                         *      MessageExt [queueId=0, storeSize=333, queueOffset=0, sysFlag=0, bornTimestamp=1496621030601, bornHost=/192.168.1.104:64083, storeTimestamp=1496621040591, storeHost=/192.168.1.141:10911, msgId=C0A8018D00002A9F0000000000031E36, commitLogOffset=204342, bodyCRC=1416242975, reconsumeTimes=1, preparedTransactionOffset=0, toString()=Message [topic=TopicTest1, flag=0, properties={MIN_OFFSET=0, REAL_TOPIC=%RETRY%ProducerGroupName, ORIGIN_MESSAGE_ID=C0A8018D00002A9F00000000000319FA, RETRY_TOPIC=TopicTest1, MAX_OFFSET=1, KEYS=OrderID001, CONSUME_START_TIME=1496621040788, UNIQ_KEY=C0A8016838E814DAD5DC165468C90005, WAIT=false, DELAY=3, TAGS=TagB, REAL_QID=0}, body=10]]
                         *  消息体中：reconsumeTimes表示重试的次数
                         */
                        throw new Exception("消息处理失败");
                    }

                }catch (Exception ex){
                    ex.printStackTrace();

                    /**
                     * 可以根据自己业务需要，修改重试机制，当次数达到一定数量之后，不再进行重试，改为其他处理（比如入库）；无需等待10次重试之后再废弃
                     */
                    if(msg!=null && msg.getReconsumeTimes()>5){
                        //记录日志 。。。，并且保证数据到数据库中，不在重试
                        //saveMessageTODB()
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
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
