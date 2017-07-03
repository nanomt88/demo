package com.nanomt88.demo.rocketmq.dao;

import com.nanomt88.demo.boot.Main;
import com.nanomt88.demo.rocketmq.entity.EventConsumer;
import com.nanomt88.demo.rocketmq.entity.EventConsumerTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(value = SpringJUnit4ClassRunner.class)
@Transactional
@Rollback(value = false)
@SpringBootTest(classes = Main.class)  // 指定spring-boot的启动类
public class EventConsumerDaoTest {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumerDaoTest.class);

    @Autowired
    private EventConsumerDao eventConsumerDao;

    @Autowired
    private EventConsumerTaskDao eventConsumerTaskDao;

    @Autowired
    private EventConsumerCustomDao eventConsumerCustomDao;

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

    @Test
    public void testBatch(){
        long begin = System.currentTimeMillis();

        for (int i = 0; i < 1000000; i++) {

            EventConsumer s = new EventConsumer();
            s.setMsgExtra("{\"MIN_OFFSET\":\"0\",\"MAX_OFFSET\":\"1078\",\"KEYS\":\"a4d8747e-0e79-415a-bbd4-77a167314c4d\",\"TRAN_MSG\":\"true\",\"CONSUME_START_TIME\":\"1498406410261\",\"id\":\"15015\",\"UNIQ_KEY\":\"C0A80170077C14DAD5DC80BF24020002\",\"PGROUP\":\"producerGroup\",\"TAGS\":\"tag\"}");
            s.setMsgBody("{\"amount\":\"1\",\"payType\":\"OUT\",\"balanceMode\":\"IN\",\"username\":\"王五\"}");
            s.setMsgKey("testTopic_key"+i);
            s.setTopic("testTopic_data");
            s.setCreateTime(new Date());
            eventConsumerDao.save(s);

            if(i % 100 == 0){
                eventConsumerDao.flush();
            }
            if(i % 10000 == 0){
                System.out.println( (i/10000) +"%");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("=======================>耗时："+(end-begin) + "ms");
    }

    @Test
    public  void testSave(){
        eventConsumerCustomDao.batchSave();
    }
}