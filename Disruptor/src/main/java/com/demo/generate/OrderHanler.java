package com.demo.generate;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import java.util.UUID;

/**
 * Created by ZBOOK-17 on 2017/4/10.
 */
public class OrderHanler implements EventHandler<Order>,WorkHandler<Order> {
    @Override
    public void onEvent(Order event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    @Override
    public void onEvent(Order event) throws Exception {
        //模拟简单的业务逻辑，设置订单值
        event.setId(UUID.randomUUID().toString());
        System.out.println(event.toString());
    }
}
