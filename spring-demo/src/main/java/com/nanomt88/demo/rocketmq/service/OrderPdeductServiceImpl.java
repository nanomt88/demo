package com.nanomt88.demo.rocketmq.service;

import com.nanomt88.demo.rocketmq.dao.OrderPdeductDao;
import com.nanomt88.demo.rocketmq.dao.OrderPdeductDaoCustom;
import com.nanomt88.demo.rocketmq.entity.OrderPdeduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/6/15 下午3:27
 * @Description: //TODO
 */

@Service
@Transactional(rollbackFor = Throwable.class)
public class OrderPdeductServiceImpl implements OrderPdeductService{

    @Autowired
    private OrderPdeductDaoCustom orderPdeductDaoCustom;

    @Autowired
    private OrderPdeductDao orderPdeductDao;

    @Override
    public void save(OrderPdeduct orderPdeduct) {
        orderPdeductDao.save(orderPdeduct);
    }

    @Override
    public OrderPdeduct getOne(Long id) {
        List<OrderPdeduct> list = orderPdeductDaoCustom.getOrderById(id);
        return list==null || list.size()<1 ? null : list.get(0);
    }
}
