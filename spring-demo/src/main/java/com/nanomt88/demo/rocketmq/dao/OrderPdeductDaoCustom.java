package com.nanomt88.demo.rocketmq.dao;

import com.lxft.nova.commons.criteria.pdeduct.RealtimePdeductListQueryCriteria;
import com.lxft.nova.commons.criteria.pdeduct.ReturnFileOrderListCriteria;
import com.nanomt88.demo.rocketmq.entity.OrderPdeduct;

import java.util.List;

/**
 * Created by whl on 3/3/17.
 */
public interface OrderPdeductDaoCustom {

    List<OrderPdeduct> getOrderById(Long id);

    Long count(RealtimePdeductListQueryCriteria criteria);

    List<OrderPdeduct> returnFileOrderlist(ReturnFileOrderListCriteria criteria);

    List<OrderPdeduct> billFileOrderlist(ReturnFileOrderListCriteria criteria);

    List<OrderPdeduct> settleOrderlist(ReturnFileOrderListCriteria criteria);

}
