package com.nanomt88.demo.rocketmq.consumer;


import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 生产者
 */
@Component
public class Producer {

    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    private DefaultMQProducer defaultMQProducer;

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

        defaultMQProducer = new DefaultMQProducer(groupName);
        defaultMQProducer.setNamesrvAddr(addr);
        defaultMQProducer.setInstanceName(instanceName);
        //关闭VIP通道，避免出现connect to <:10909> failed导致消息发送失败
        defaultMQProducer.setVipChannelEnabled(false);
        defaultMQProducer.start();
        logger.info("RocketMq Producer start success");
    }

    @PreDestroy
    public void destroy() {
        defaultMQProducer.shutdown();
    }

    public DefaultMQProducer getDefaultMQProducer() {
        return defaultMQProducer;
    }

}
