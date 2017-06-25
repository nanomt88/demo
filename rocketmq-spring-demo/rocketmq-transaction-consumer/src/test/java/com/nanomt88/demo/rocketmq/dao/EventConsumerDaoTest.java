package com.nanomt88.demo.rocketmq.dao;

import com.nanomt88.demo.boot.Main;
import com.nanomt88.demo.rocketmq.entity.EventConsumerTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(value = SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest(classes = Main.class)  // 指定spring-boot的启动类
public class EventConsumerDaoTest {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumerDaoTest.class);

    @Autowired
    private EventConsumerDao eventConsumerDao;

    @Autowired
    private EventConsumerTaskDao eventConsumerTaskDao;

    @Test
    public void findIdByTopicAndCreateTimeAfter() throws Exception {
        List<String> count = eventConsumerDao.findMsgKeyByTopicAndCreateTimeGreaterThan("payTopic", new Date(0));

        logger.info("==============>count:{}", count);
    }

    @Test
    public void findConsumerTask(){
        EventConsumerTask one = eventConsumerTaskDao.findOne("payTopic");
        System.out.println("=================>"+ one);
//        EventConsumerTask two = eventConsumerTaskDao.getOne("payTopic");
//        System.out.println("=================>"+two);
    }
}