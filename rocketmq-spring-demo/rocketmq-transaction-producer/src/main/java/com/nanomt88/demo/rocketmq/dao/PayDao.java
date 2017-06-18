package com.nanomt88.demo.rocketmq.dao;

import com.nanomt88.demo.rocketmq.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * Created by ZBOOK-17 on 2017/6/12.
 */
@Repository
public interface PayDao extends JpaRepository<Pay, Long> {

    @Modifying(clearAutomatically = true)
    @Query(" update Pay b set b.amount=b.amount+:amount where b.username=:username")
    int updateAmountByUsername(@Param("amount") BigDecimal amount, @Param("username") String username);

    Pay findByUsername(String username);

    Pay findByUserId(String userId);
}
