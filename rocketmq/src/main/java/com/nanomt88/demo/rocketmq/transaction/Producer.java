package com.nanomt88.demo.rocketmq.transaction;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事物消息 demo
 *      事物消息是通过两阶段提交来实现事物消息的。在 Prepared阶段：先发送消息到mq，消息标记为prepared状态；消息存储在commit log中，
 *      但是在consumer Queue中没有，所以consumer端不会接受到此消息。 等到本地事物提交之后，mq会再调用producer端 确认消息的状态。
 *
 */
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    public static void main(String[] args) throws MQClientException, InterruptedException {
        /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ProducerGroupName需要由应用来保证唯一<br>
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         *
         * TransactionMQProducer ： 发送事物消息
         */
        TransactionMQProducer producer = new TransactionMQProducer("ProducerGroupName");

        producer.setNamesrvAddr("192.168.1.140:9876;192.168.1.141:9876");

        /**
         * 这个监听器接口是在LocalTransactionExecuter 返回UNKNOW的时候（或者因为网路或者其他原因没有返回；或者mq没有收到返回的消息），再查询消息状态的接口
         * 注意：
         *      阿里在RocketMQ 3.0.6之后阉割了此功能，所以此功能不能用。要保证严格的消息不丢失，需要自己处理
         */
        producer.setTransactionCheckListener(new TransactionCheckListener() {
            @Override
            public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
                logger.info("消息回查监听：ID[{}], msg[{}]", msg.getKeys(), msg);
                if ("order_1".equals(msg.getKeys())) {
                    logger.info("");
                    return LocalTransactionState.COMMIT_MESSAGE;
                } else if ("order_2".equals(msg.getKeys())) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                return LocalTransactionState.UNKNOW;
            }
        });

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
        for (int i = 0; i < 2; i++) {

            Message msg = new Message("TransactionTopic",// topic
                    "TagA",// tag
                    "cccc_" + i,// key ： 用于唯一标记，方便去重
                    ("Hello MetaQ").getBytes());// body

            /**
             * 事物消息的确认阶段： 事物消息通过回调 LocalTransactionExecuter.executeLocalTransactionBranch 方法来确认消息的状态，来决定消息是否提交或者回滚。
             *      提交消息之后，consumer端才能收到消息；ROLLBACK_MESSAGE 后消息就会回滚。
             */
            SendResult sendResult = producer.sendMessageInTransaction(msg, new LocalTransactionExecuter() {
                /**
                 *
                 * @param msg
                 * @param arg   此处为sendMessage时传递过来的扩展参数
                 * @return
                 */
                @Override
                public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logger.info("消息确认：ID[{}], msg[{}]", msg.getKeys(), msg);
                    if("order_1".equals(msg.getKeys())){
                        //返回 COMMIT_MESSAGE 之后，消息才会提交，consumer端才能收到消息
                        return LocalTransactionState.COMMIT_MESSAGE;
                    }
                    //返回UNKONW之后，消息会丢失，mq不会接着回查消息状态，需要自己通过其他手段保证消息的一致性。
                    return LocalTransactionState.UNKNOW;
                }
            }, 0);
            logger.info("发送消息：ID[{}], msg[{}]",  msg.getKeys(), msg);

        }

        Thread.sleep(Integer.MAX_VALUE);

        /**
         * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己
         * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
         */
        producer.shutdown();

    }
}
