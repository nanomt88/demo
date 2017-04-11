package com.demo.product;

import com.demo.generate.Order;
import com.lmax.disruptor.EventHandler;

/**
 * Created by ZBOOK-17 on 2017/4/11.
 */
public class OrderHandle4 implements EventHandler<Order>  {

    @Override
    public void onEvent(Order event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("OrderHandle5 : get price : " + event.getPrice());
        event.setPrice(event.getPrice()+999);
    }
}
