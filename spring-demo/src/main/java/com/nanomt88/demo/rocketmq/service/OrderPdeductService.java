package com.nanomt88.demo.rocketmq.service;

import com.nanomt88.demo.rocketmq.entity.OrderPdeduct;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/6/15 下午3:26
 * @Description: //TODO
 */

public interface OrderPdeductService {

    void save(OrderPdeduct orderPdeduct);

    OrderPdeduct getOne(Long id);

}
