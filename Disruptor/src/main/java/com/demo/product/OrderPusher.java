package com.demo.product;

import com.demo.generate.Order;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 生产者 生产消息
 */
public class OrderPusher implements Runnable {

    private CountDownLatch latch;
    Disruptor<Order> disruptor ;

    private static int LOOP = 10;

    public OrderPusher(Disruptor<Order> disruptor, CountDownLatch countDownLatch){
        this.disruptor = disruptor;
        this.latch = countDownLatch;
    }

    @Override
    public void run() {
        OrderEventTranslator translator = new OrderEventTranslator();
        for (int i = 0; i < LOOP; i++) {
            disruptor.publishEvent(translator);
        }
        //生产完成之后 发送通知
        latch.countDown();
    }
}

class OrderEventTranslator implements EventTranslator<Order>{

    private Random random = new Random();

    @Override
    public void translateTo(Order event, long sequence) {
        event.setPrice(random.nextDouble() * 9999);
    }
}
