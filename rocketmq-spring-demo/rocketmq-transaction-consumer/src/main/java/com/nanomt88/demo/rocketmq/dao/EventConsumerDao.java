package com.nanomt88.demo.rocketmq.dao;

import com.nanomt88.demo.rocketmq.entity.EventConsumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 *
 * @author nanomt88@gmail.com
 * @create 2017-06-19 22:24
 **/

@Repository
public interface EventConsumerDao extends JpaRepository<EventConsumer, Long> {

    @Query("select a.msgKey from EventConsumer a where a.topic=:topic and a.createTime >= :updateTime order by id desc")
    List<String> findMsgKeyByTopicAndCreateTimeGreaterThan(@Param("topic") String topic , @Param("updateTime")Date updateTime);

    EventConsumer findByTopicAndMsgKey(String topic, String msgKey);
}
