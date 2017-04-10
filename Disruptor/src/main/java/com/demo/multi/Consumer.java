package com.demo.multi;

import com.lmax.disruptor.WorkHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/10 上午7:40
 * @Description:    消费者
 */

public class Consumer implements WorkHandler<Order> {

    private String consumerId;

    private static AtomicInteger count = new AtomicInteger(0);

    public Consumer(String consumerId){
        this.consumerId = consumerId;
    }

    /**
     * Callback to indicate a unit of work needs to be processed.
     *
     * @param event published to the {@link RingBuffer}
     * @throws Exception if the {@link WorkHandler} would like the exception handled further up the chain.
     */
    @Override
    public void onEvent(Order event) throws Exception {
        System.out.println("当前消费者：" + consumerId + "，消费信息：" + event.toString());
        count.incrementAndGet();
    }


    public int getCount(){
        return count.get();
    }
}
