package com.nanomt88.demo.rocketmq.dao;

import com.nanomt88.demo.rocketmq.entity.EventProducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nanomt88@gmail.com
 * @create 2017-06-19 22:24
 **/
@Repository
public interface EventProducerDao  extends JpaRepository<EventProducer, Long> {

}
