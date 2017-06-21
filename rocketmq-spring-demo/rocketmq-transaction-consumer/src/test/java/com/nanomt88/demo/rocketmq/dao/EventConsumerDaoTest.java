package com.nanomt88.demo.rocketmq.dao;

import com.nanomt88.demo.boot.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(value = SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest(classes = Main.class)  // 指定spring-boot的启动类
public class EventConsumerDaoTest {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumerDaoTest.class);

    @Autowired
    private EventConsumerDao eventConsumerDao;

    @Test
    public void findIdByTopicAndCreateTimeAfter() throws Exception {
        Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
        List<Long> list = eventConsumerDao.findIdsByTopicAndCreateTimeAfter("payTopic", date);
        logger.info("id:{}", list);
    }

}