package com.nanomt88.demo.rocketmq.dao;

import com.nanomt88.demo.rocketmq.common.MessageStatus;
import com.nanomt88.demo.rocketmq.entity.EventProducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
public interface EventProducerDao  extends JpaRepository<EventProducer, Long> {

    EventProducer findByTopicAndMsgKey(String topic, String msgKey);

    @Modifying(clearAutomatically = true)
    @Query(" update EventProducer set status=:status where topic=:topic and msgKey=:msgKey ")
    int updateStatusByTopicAndMsgKey(@Param("status") MessageStatus status, @Param("topic") String topic, @Param("msgKey") String msgKey);

    @Modifying(clearAutomatically = true)
    @Query(" update EventProducer set status=:status where topic=:topic and status=0 and msgKey in :msgKey  ")
    int updatePreparedEventByTopicAndMsgKeyIn(@Param("status") MessageStatus status, @Param("topic") String topic, @Param("msgKey")List<String> msgKeys);

    @Query(" select e from EventProducer e where status=:status and topic=:topic and e.createTime <= :createTime ")
    List<EventProducer> findByTopicAndStatusAndCreateTimeLessThanEqual(@Param("topic") String topic, @Param("status") MessageStatus status
                    ,@Param("createTime") Date createTime);
}
