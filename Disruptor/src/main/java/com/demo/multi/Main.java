package com.demo.multi;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/10 上午7:33
 * @Description: //TODO
 */

public class Main {

    public static void main(String[] args) throws InterruptedException {
        //创建事件发布管理器
        RingBuffer<Order> ringBuffer = RingBuffer.create(ProducerType.SINGLE, new EventFactory<Order>() {
            @Override
            public Order newInstance() {
                return new Order();
            }
        }, 1024, new YieldingWaitStrategy());
        //用于管理多个消费者，维护序列
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //创建多个消费者
        Consumer[] consumers = new Consumer[3];
        for (int i = 0; i < 3; i++) {
            consumers[i] = new Consumer("consumer"+i);
        }

        //工作池，类似于线程池？？
        WorkerPool workerPool = new WorkerPool(ringBuffer, sequenceBarrier, new ExceptionHandler() {
            @Override
            public void handleEventException(Throwable ex, long sequence, Object event) {
                ex.printStackTrace();
            }

            @Override
            public void handleOnStartException(Throwable ex) {
                ex.printStackTrace();
            }

            @Override
            public void handleOnShutdownException(Throwable ex) {
                ex.printStackTrace();
            }
        }, consumers);
        //????  这个貌似很重要啊？
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

        CyclicBarrier cyclicBarrier = new CyclicBarrier(100);
        for(int i = 0; i < 100 ; i++){
            final Producer p = new Producer(ringBuffer);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    for(int j = 0; j<100; j++ ) {
                        p.onData(UUID.randomUUID().toString());
                    }
                }
            }).start();
        }

        System.out.println("----------程序开始执行-------------");
        Thread.sleep(6000);
        System.out.println("total: " + consumers[0].getCount());
    }
}
