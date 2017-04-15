package com.demo.test;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用 disruptor 处理复杂逻辑 示例
 */
public class Main1 {

    public static void main(String[] args) throws InterruptedException {

        int bufferSize = 1024;

        ExecutorService executorService = Executors.newFixedThreadPool(8);

        EventFactory<TestEvent> eventFactory = new EventFactory<TestEvent>() {
            @Override
            public TestEvent newInstance() {
                return new TestEvent();
            }
        };
//         BusySpinWaitStrategy ： 自旋等待，类似Linux Kernel使用的自旋锁。低延迟但同时对CPU资源的占用也多。
        Disruptor<TestEvent> disruptor = new Disruptor<TestEvent>(eventFactory , bufferSize, executorService,
                    ProducerType.SINGLE, new BusySpinWaitStrategy());

        //菱形操作
        //1. 使用disruptor创建消费者组C1、C2， handleEventsWith 并行执行
        //2. 声明在C1/C2完事之后执行JMS消息发送操作 ： 也就是流程走到C3
        EventHandlerGroup handlerGroup = disruptor.handleEventsWith(new Handle1(),new Handle2());
        handlerGroup.then(new Handle3());

        //启动
        long start = System.currentTimeMillis();

        disruptor.start();

        final CountDownLatch latch = new CountDownLatch(1);
        //生产者准备
        executorService.submit(new TestPusher(disruptor,latch)) ;

        latch.await(); //等待生产者完成
        //调用shutdown 不会马上停止，会等到消费者都消费完消息之后才会停止
        disruptor.shutdown();
        executorService.shutdown();
        System.out.println("总耗时："+( System.currentTimeMillis() - start ));
    }
}
