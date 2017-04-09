package com.demo.helloworld;

import com.lmax.disruptor.EventHandler;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/9 上午9:17
 * @Description:
 *           这是一个事件的消费者，也就是一个事件处理器。这个事件处理器简单的把传递过来的值打印到控制台
 */

public class LongEventConsumer implements EventHandler<LongEvent>{
    /**
     * Called when a publisher has published an event to the {@link RingBuffer}
     *
     * @param event      published to the {@link RingBuffer}
     * @param sequence   of the event being processed
     * @param endOfBatch flag to indicate if this is the last event in a batch from the {@link RingBuffer}
     * @throws Exception if the EventHandler would like the exception handled further up the chain.
     */
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(Thread.currentThread().getName() + "  sequence: " + sequence + "  value: " + event.getValue());
    }
}
