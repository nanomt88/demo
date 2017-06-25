package com.nanomt88.demo.rocketmq.consumer;


import org.apache.rocketmq.client.QueryResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 生产者
 */
@Component
public class MQProducer {

    private final Logger logger = LoggerFactory.getLogger(MQProducer.class);

    private DefaultMQProducer producer;

    @Autowired
    private Environment env;

    @PostConstruct
    public void init() throws MQClientException {

        String groupName = env.getProperty("rocketmq.sync.task.producerGroup");
        String addr = env.getProperty("rocketmq.namesrvAddr");
        //String instanceName = env.getProperty("rocketmq.instanceName");

        assert groupName != null;
        assert addr != null;
        //assert instanceName != null;

        producer = new DefaultMQProducer(groupName);
        producer.setNamesrvAddr(addr);
       // producer.setInstanceName(instanceName);
        //关闭VIP通道，避免出现connect to <:10909> failed导致消息发送失败
        producer.setVipChannelEnabled(false);
        producer.setRetryTimesWhenSendFailed(3);

        producer.start();
        logger.info("RocketMq client start success");
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
        return producer.queryMessage(topic, key, maxNum, begin, end);
    }

    /**
     * 给 RocketMQ 发送消息
     * @param message
     */
    public SendResult sendMessage(Message message) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        SendResult send = producer.send(message);
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
        logger.info("消息发送成功：{}，返回结果：{}",message.getKeys(), send);
        return send;
    }

    @PreDestroy
    public void destroy() {
        producer.shutdown();
    }

    public void shutdown(){
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                producer.shutdown();
            }
        }));
        System.exit(0);
    }

    public DefaultMQProducer getproducer() {
        return producer;
    }

}
