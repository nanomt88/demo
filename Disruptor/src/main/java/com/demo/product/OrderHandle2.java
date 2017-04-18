package com.demo.product;

import com.demo.generate.Order;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * Created by ZBOOK-17 on 2017/4/11.
 */
public class OrderHandle2 implements EventHandler<Order>  {

    @Override
    public void onEvent(Order event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(Thread.currentThread().getName()+"  OrderHandle2 : set price");
        event.setPrice(100);
        Thread.sleep(5000);
    }
}
