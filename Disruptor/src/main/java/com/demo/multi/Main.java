package com.demo.multi;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/10 上午7:33
 * @Description: //TODO
 */

public class Main {

    public static void main(String[] args) throws InterruptedException {
        //创建事件发布管理器
        // RingBuffer: 被看作disruptor中最主要的组件。然而3.0之后开始，RingBuffer仅仅负责存储和更新的disruptor中流通
        // 的数据。对一些特殊的应用场景可以被用户（使用其他数据结构）完全替代
        //ProducerType.MULTI : 如果生产者是多线程的话，这个地方一定要设置为MULTI 多线程模式，否则会出现漏掉没执行的问题
        RingBuffer<Order> ringBuffer = RingBuffer.create(ProducerType.MULTI, new EventFactory<Order>() {
            @Override
            public Order newInstance() {
                return new Order();
            }
        }, 1024*1024, new YieldingWaitStrategy());

        //  SequenceBarrier：由Sequence生成，并且包含了已经发布的Sequence的引用，这些Sequence源于Sequencer和一些独立的
        //  消费者的Sequence。它包含了决定是否有供消费者来消费的Event的逻辑
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //创建多个消费者
        Consumer[] consumers = new Consumer[3];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer("c"+i);
        }
        //WorkProcessor : 确保每个sequence只被一个processor消费，在同一个workPool中处理的多个workProcessor不会消费同样的sequence
        //WorkPool： 一个workProcessor池，其中workProcessor将消费sequence，所以任务可以在实现workHandler接口的worker直接移交
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
        //TODO 这句话干嘛的啊？？  这个貌似很重要啊？
        // workerPool.getWorkerSequences()： 获取生产者对应的三个sequence
        //addGatingSequences ： 设置生产者根据各自对应的sequence 调节各自对应的速度，维持生产者和消费者的顺序执行
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        //启动 workPool
        workerPool.start(executorService);

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(100);
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
        Thread.sleep(5000);
        System.out.println("total: " + consumers[0].getCount());
    }
}
