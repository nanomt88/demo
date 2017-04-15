package com.demo.product;

import com.demo.generate.Order;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 使用 disruptor 处理复杂逻辑 示例
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        long beginTime = System.currentTimeMillis();
        int bufferSize = 1024;

        ExecutorService executorService = Executors.newFixedThreadPool(8);

        EventFactory<Order> eventFactory = new EventFactory<Order>() {
            @Override
            public Order newInstance() {
                return new Order();
            }
        };
//         BusySpinWaitStrategy ： 自旋等待，类似Linux Kernel使用的自旋锁。低延迟但同时对CPU资源的占用也多。
        Disruptor<Order> disruptor = new Disruptor<Order>(eventFactory , bufferSize, executorService,
                    ProducerType.SINGLE, new BusySpinWaitStrategy());

//        ThreadFactory threadFactory = new ThreadFactory() {
//            @Override
//            public Thread newThread(Runnable r) {
//                return new Thread(r);
//            }
//        };
//
//        Disruptor<Order> disruptor = new Disruptor<Order>(eventFactory, bufferSize, threadFactory ,
//                ProducerType.SINGLE, new BusySpinWaitStrategy());

        //菱形操作
        //1. 使用disruptor创建消费者组C1、C2， handleEventsWith 并行执行
        //2. 声明在C1/C2完事之后执行JMS消息发送操作 ： 也就是流程走到C3
        EventHandlerGroup handlerGroup = disruptor.handleEventsWith(new OrderHandle1(),new OrderHandle2());
        handlerGroup.then(new OrderHandle3());

        //顺序操作
//        disruptor.handleEventsWith(new OrderHandle1())
//                .handleEventsWith(new OrderHandle2())
//                .handleEventsWith(new OrderHandle3());

        //六边形处理
//        OrderHandle1 h1 = new OrderHandle1();
//        OrderHandle2 h2 = new OrderHandle2();
//        OrderHandle3 h3 = new OrderHandle3();
//        OrderHandle4 h4 = new OrderHandle4();
//        OrderHandle5 h5 = new OrderHandle5();
//        disruptor.handleEventsWith(h1,h2);
//        disruptor.after(h1).handleEventsWith(h4);
//        disruptor.after(h2).handleEventsWith(h5);
//        disruptor.after(h4,h5).handleEventsWith(h3);

        //启动
        disruptor.start();

        final CountDownLatch latch = new CountDownLatch(1);
        //生产者准备
        executorService.submit(new OrderPusher(disruptor,latch)) ;

        latch.await(); //等待生产者完成
        //调用shutdown 不会马上停止，会等到消费者都消费完消息之后才会停止
        disruptor.shutdown();
        executorService.shutdown();
        System.out.println("总耗时："+( System.currentTimeMillis() - beginTime ));
    }
}
