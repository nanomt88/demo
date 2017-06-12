package com.nanomt88.demo.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 消息消费者
 */
//@Component
public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private DefaultMQPushConsumer defaultMQPushConsumer;

    @Autowired
    private Environment env;

    @Autowired
    private RocketMqMessageWrapper rocketMqMessageWrapper;

    /*
     * PostConstruct 注释用于在依赖关系注入完成之后需要执行的方法上，以执行任何初始化。此方法必须在将类放入服务之前调用。
     * 支持依赖关系注入的所有类都必须支持此注释。即使类没有请求注入任何资源，用 PostConstruct 注释的方法也必须被调用。
     * 只有一个方法可以用此注释进行注释。应用 PostConstruct 注释的方法必须遵守以下所有标准：该方法不得有任何参数，
     * 除非是在 EJB 拦截器 (interceptor) 的情况下，根据 EJB 规范的定义，在这种情况下它将带有一个 InvocationContext 对象 ；
     * 该方法的返回类型必须为 void；该方法不得抛出已检查异常；应用 PostConstruct 的方法可以是 public、protected、package private
     * 或 private；除了应用程序客户端之外，该方法不能是 static；该方法可以是 final；如果该方法抛出未检查异常，
     * 那么不得将类放入服务中，除非是能够处理异常并可从中恢复的 EJB。
     *
     */
    @PostConstruct
    public void init() throws MQClientException {

        String groupName = env.getProperty("rocketmq.consumerGroup");
        String addr = env.getProperty("rocketmq.namesrvAddr");
        String instanceName = env.getProperty("rocketmq.instanceName");
        String topic = env.getProperty("rocketmq.topic");
        String expression = env.getProperty("rocketmq.topic.expression");

        assert groupName != null;
        assert addr != null;
        assert topic != null;
        assert expression != null;
        assert instanceName != null;


        defaultMQPushConsumer = new DefaultMQPushConsumer(groupName);

        defaultMQPushConsumer.setNamesrvAddr(addr);
        defaultMQPushConsumer.setInstanceName(instanceName);

        // consumer 配置参数
        /**
         * 设置consumer启动后是从头部开始消费还是从队列尾部开始消费
         * 如果非第一次启动，需要按照上一次消费的位置进行消费
         */
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        /**
         *  设置 消费线程池最小数量
         *  consumer.setConsumeThreadMin(5);
         *
         *  设置 消费线程池最大数量
         *  consumer.setConsumeThreadMax(20);
         */

        /**
         * 设置 在Push模式下，一次最多拉取的消息数量
         */
        defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);

        defaultMQPushConsumer.subscribe(topic, expression);

        //设置为集群消费(区别于广播消费)
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);

        //vipchannel是用于读写分离到。
        //关闭VIP通道，避免接收不了消息
        defaultMQPushConsumer.setVipChannelEnabled(false);

        //注册从broker读取到消息之后，处理消息的回调事件
        defaultMQPushConsumer.registerMessageListener(rocketMqMessageWrapper);

        defaultMQPushConsumer.start();
        logger.info("rocketMQ Client start success");
    }

    @PreDestroy
    public void destroy() {
        defaultMQPushConsumer.shutdown();
    }
}
