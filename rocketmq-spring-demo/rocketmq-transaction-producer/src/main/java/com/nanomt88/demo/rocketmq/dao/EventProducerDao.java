package com.nanomt88.demo.rocketmq.dao;

import com.nanomt88.demo.rocketmq.common.MessageStatus;
import com.nanomt88.demo.rocketmq.entity.EventProducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nanomt88@gmail.com
 * @create 2017-06-19 22:24
 **/
@Repository
public interface EventProducerDao  extends JpaRepository<EventProducer, Long> {

    EventProducer findByTopicAndMsgKey(String topic, String msgKey);

    @Modifying
    @Query(" update EventProducer set status=:status where topic=:topic and msgKey=:msgKey ")
    int updateStatusByTopicAdnMsgKey(@Param("status") MessageStatus status, @Param("topic") String topic, @Param("msgKey") String msgKey);

}
