package com.nanomt88.demo.rocketmq.dao;

import com.lxft.nova.commons.type.pdeduct.OrderPdeductStatus;
import com.nanomt88.demo.rocketmq.entity.OrderPdeduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: nanomt88	<br/>
 * Date: 2017/3/1 下午4:38	<br/>
 * Email: nanomt88@gmail.com  <br/>
 * Description: 实时代扣订单表
 */
@Repository(value = "OrderPdeductDao")
public interface OrderPdeductDao extends CrudRepository<OrderPdeduct, Long>,JpaRepository<OrderPdeduct, Long>,JpaSpecificationExecutor<OrderPdeduct> {

	@Query("select a from OrderPdeduct a where a.orderId = :orderId")
	OrderPdeduct getByOrderId(@Param("orderId") String orderId);

    Page<OrderPdeduct> findAll(Specification<OrderPdeduct> spec, Pageable pageable);

    @Query("select a from OrderPdeduct a where a.createTimeIndex>=:time and a.status=:status")
    List<OrderPdeduct> findByTimeAndStatus(@Param("time") Long time, @Param("status") OrderPdeductStatus status);

	@Modifying
	@Query("update OrderPdeduct o set o.status=:status where o.orderId=:orderId")
	int updateStatusByOrderId(@Param("status") OrderPdeductStatus status, @Param("orderId") String orderId);

	OrderPdeduct findByMerchantIdAndMerchantOrderNo(String merchantId, String merchantOrderNo);

	List<OrderPdeduct> findByMerchantIdAndStatusAndDate(String merchantId, OrderPdeductStatus orderPdeductStatus, Integer date);

	List<OrderPdeduct> findByDateAndStatus(Integer date, OrderPdeductStatus orderPdeductStatus);

	List<OrderPdeduct> findByStatus(OrderPdeductStatus waitRefund);
}
