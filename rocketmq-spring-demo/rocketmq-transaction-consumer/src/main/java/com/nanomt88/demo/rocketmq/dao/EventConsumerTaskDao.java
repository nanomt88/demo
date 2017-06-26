package com.nanomt88.demo.rocketmq.dao;

import com.nanomt88.demo.rocketmq.entity.EventConsumerTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

/**
 * @author nanomt88@gmail.com
 * @create 2017-06-19 23:04
 **/
@Repository
public interface EventConsumerTaskDao extends JpaRepository<EventConsumerTask, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from EventConsumerTask e where e.topic = :topic")
    EventConsumerTask findByTopicForUpdate(@Param("topic") String topic);
}
