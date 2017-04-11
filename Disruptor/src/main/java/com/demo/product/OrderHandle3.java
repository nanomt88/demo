package com.demo.product;

import com.demo.generate.Order;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * Created by ZBOOK-17 on 2017/4/11.
 */
public class OrderHandle3 implements EventHandler<Order>  {
    @Override
    public void onEvent(Order event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("OrderHandle3 : "+event.toString());
    }

}
