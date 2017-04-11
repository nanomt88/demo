package com.demo.product;

import com.demo.generate.Order;
import com.lmax.disruptor.EventHandler;

/**
 * Created by ZBOOK-17 on 2017/4/11.
 */
public class OrderHandle5 implements EventHandler<Order>  {

    @Override
    public void onEvent(Order event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("OrderHandle4 : get name : " + event.getName());
        event.setName("name4");
    }
}
