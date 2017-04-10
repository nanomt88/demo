package com.demo.generate;

import com.lmax.disruptor.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  单生产者 单消费者实例
 *   使用WorkerPool 处理消息
 */
public class Main2 {

    public static void main(String[] args) throws InterruptedException {

        int BUFFER_SIZE = 1024;
        int THREAD_NUM = 4;

        RingBuffer<Order> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<Order>() {
            @Override
            public Order newInstance() {
                return new Order();
            }
        }, BUFFER_SIZE);

        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);

        WorkerPool workerPool = new WorkerPool(ringBuffer, sequenceBarrier,
                new IgnoreExceptionHandler(), new OrderHanler());

        workerPool.start(executorService);

        for (int i = 0; i < 10; i++) {
            long sequence = ringBuffer.next();
            ringBuffer.get(sequence).setPrice(Math.random() *9999);
            ringBuffer.publish(sequence);
        }

        Thread.sleep(1000);
        workerPool.halt();
        executorService.shutdown();
    }
}
