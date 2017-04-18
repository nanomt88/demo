package com.demo.test;

import com.demo.generate.Order;
import com.lmax.disruptor.EventHandler;

/**
 * Created by ZBOOK-17 on 2017/4/11.
 */
public class Handle2 implements EventHandler<TestEvent>  {

    @Override
    public void onEvent(TestEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(Thread.currentThread().getName()+"  Handle2 : set price");
        event.setPrice(100*sequence);
        Thread.sleep(20);
    }
}
