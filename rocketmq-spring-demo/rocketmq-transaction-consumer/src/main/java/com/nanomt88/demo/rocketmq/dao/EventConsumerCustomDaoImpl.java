package com.nanomt88.demo.rocketmq.dao;

import com.nanomt88.demo.rocketmq.entity.EventConsumer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

/**
 * @author nanomt88@gmail.com
 * @create 2017-06-27 8:23
 **/
@Repository
public class EventConsumerCustomDaoImpl implements EventConsumerCustomDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void batchSave() {

        long begin = System.currentTimeMillis();

        for (int i = 1; i <= 1000000; i++) {

            EventConsumer s = new EventConsumer();
            s.setMsgExtra("{\"MIN_OFFSET\":\"0\",\"MAX_OFFSET\":\"1078\",\"KEYS\":\"a4d8747e-0e79-415a-bbd4-77a167314c4d\",\"TRAN_MSG\":\"true\",\"CONSUME_START_TIME\":\"1498406410261\",\"id\":\"15015\",\"UNIQ_KEY\":\"C0A80170077C14DAD5DC80BF24020002\",\"PGROUP\":\"producerGroup\",\"TAGS\":\"tag\"}");
            s.setMsgBody("{\"amount\":\"1\",\"payType\":\"OUT\",\"balanceMode\":\"IN\",\"username\":\"王五\"}");
            s.setMsgKey("testTopic_key"+i);
            s.setTopic("testTopic_data");
            s.setCreateTime(new Date());

            em.persist(s);

            if( i % 100 == 0){
                em.flush();
                em.clear();
            }
            if(i % 10000 == 0){
                System.out.println( (i/10000) +"%");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("=======================>耗时："+(end-begin) + "ms");

    }
}
