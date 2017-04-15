package com.demo.test;

import com.demo.generate.Order;
import com.lmax.disruptor.EventHandler;

/**
 * Created by ZBOOK-17 on 2017/4/11.
 */
public class Handle3 implements EventHandler<TestEvent>  {
    @Override
    public void onEvent(TestEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("Handle3 : "+event.toString());
        Thread.sleep(20);
    }

}
