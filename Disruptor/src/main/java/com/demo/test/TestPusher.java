package com.demo.test;

import com.demo.generate.Order;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 生产者 生产消息
 */
public class TestPusher implements Runnable {

    private CountDownLatch latch;
    Disruptor<TestEvent> disruptor ;

    private static int LOOP = 1000;

    public TestPusher(Disruptor<TestEvent> disruptor, CountDownLatch countDownLatch){
        this.disruptor = disruptor;
        this.latch = countDownLatch;
    }

    @Override
    public void run() {
        TestEventTranslator translator = new TestEventTranslator();
        for (int i = 0; i < LOOP; i++) {
            disruptor.publishEvent(translator);
        }
        //生产完成之后 发送通知
        latch.countDown();
    }
}

class TestEventTranslator implements EventTranslator<TestEvent>{

    private Random random = new Random();

    @Override
    public void translateTo(TestEvent event, long sequence) {
        event.setPrice(random.nextDouble() * 9999);
    }
}
