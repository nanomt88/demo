package com.nanomt88.demo.rocketmq.consumer;


import org.apache.rocketmq.client.QueryResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 生产者
 */
@Component
public class Producer {

    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    private TransactionMQProducer defaultMQProducer;

    @Autowired
    private Environment env;

    @PostConstruct
    public void init() throws MQClientException {

        String groupName = env.getProperty("rocketmq.producerGroup");
        String addr = env.getProperty("rocketmq.namesrvAddr");
        String instanceName = env.getProperty("rocketmq.instanceName");

        assert groupName != null;
        assert addr != null;
        assert instanceName != null;

        defaultMQProducer = new TransactionMQProducer(groupName);
        defaultMQProducer.setNamesrvAddr(addr);
        defaultMQProducer.setInstanceName(instanceName);
        //关闭VIP通道，避免出现connect to <:10909> failed导致消息发送失败
        defaultMQProducer.setVipChannelEnabled(false);

        //事物回查最小并发数
        defaultMQProducer.setCheckThreadPoolMinSize(5);
        //事物回查最大并发数量
        defaultMQProducer.setCheckThreadPoolMaxSize(20);
        //事物回查队列数量
        defaultMQProducer.setCheckRequestHoldMax(2000);

        //设置服务器回调producer，检查本地事物成功还是失败
        defaultMQProducer.setTransactionCheckListener(new TransactionCheckListener() {
            @Override
            public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
                try {
                    logger.info("CHECK_STATUS:{}, message:{}",msg.getKeys(),new String(msg.getBody(), "UTF-8") );
                    return LocalTransactionState.COMMIT_MESSAGE;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        });

        defaultMQProducer.start();
        logger.info("RocketMq Producer start success");
    }

    /**
     * 查询消息
     * @param topic
     * @param key
     * @param maxNum
     * @param begin
     * @param end
     * @return
     */
    public QueryResult queryMessage(String topic, String key, int maxNum, long begin, long end) throws Exception {
        return defaultMQProducer.queryMessage(topic, key, maxNum, begin, end);
    }

    /**
     * 给 RocketMQ 发送事物消息
     * @param message
     * @param localTransactionExecuter
     * @param transactionMapArgs
     */
    public void sendTransactionMessage(Message message, LocalTransactionExecuter localTransactionExecuter,
                                       Map<String, Object> transactionMapArgs) throws MQClientException {
        TransactionSendResult transactionSendResult = defaultMQProducer.sendMessageInTransaction(message, localTransactionExecuter, transactionMapArgs);
        /**
         *  2.MQProducer：用来发送生产者中的消息，包含了start和shutdown以及各种send方法，其中send方法返回值为sendResult，
         *  里面包含着SendStatus也就是发送的状态。send 消息方法，只要不抛异常，就代表发送成功。
         *  但是发送成功会有多个状态，在 sendResult 里定义。
               SEND_OK
             消息发送成功
               FLUSH_DISK_TIMEOUT
             消息发送成功，但是服务器刷盘超时，消息已经进入服务器队列，只有此时服务器宕机，消息才会丢失
               FLUSH_SLAVE_TIMEOUT
             消息发送成功，但是服务器同步到 Slave 时超时，消息已经进入服务器队列，只有此时服务器宕机，消
             息才会丢失
               SLAVE_NOT_AVAILABLE
             消息发送成功，但是此时 slave 不可用，消息已经进入服务器队列，只有此时服务器宕机，消息才会丢
         */
        logger.info("消息发送成功：{}，返回结果：{}",message.getKeys(), transactionSendResult);
    }

    @PreDestroy
    public void destroy() {
        defaultMQProducer.shutdown();
    }

    public void shutdown(){
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                defaultMQProducer.shutdown();
            }
        }));
        System.exit(0);
    }

    public TransactionMQProducer getDefaultMQProducer() {
        return defaultMQProducer;
    }

}
