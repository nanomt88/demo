package com.demo.generate;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.*;

/**
 * 单生产者  单消费者
 *  使用BatchEventProcessor 处理消息示例
 */
public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        int BUFFER_SIZE = 1024;
        int THREAD_NUM = 4;
        //createSingleProducer 创建一个单生产者的RingBuffer
        //第一个参数EventFactory：就是创建事件的工厂类，作用它的职责就是创建事件填充RingBuffer的区块
        //第二个参数BUFFER_SIZE：设置RingBuffer的缓存区大小，它必须是2的整数倍，目的是将取模运算转成&运算提高效率
        //第三个参数WaitStrategy： 是RingBuffer的生产者在没有可用区块的时候（也就是消费者或者事件处理器 太慢）的等待策略
        final RingBuffer<Order> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<Order>() {
            @Override
            public Order newInstance() {
                return new Order();
            }
        }, BUFFER_SIZE, new YieldingWaitStrategy());
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);
        //创建SequenceBarrier
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //创建消息处理器：批处理
        BatchEventProcessor<Order> transProcessor = new BatchEventProcessor<Order>(ringBuffer,sequenceBarrier,new OrderHanler());

        //这一步的目的是把消费者的sequence位置信息注入到生产者，如果只有一个消费者的情况下可以忽略
        ringBuffer.addGatingSequences(transProcessor.getSequence());

        // 把消息处理器提交到线程池
        executorService.submit(transProcessor);

        //如果存在多个消费者，那么需要重复执行上面三行代码多次，把OrderHandle换成其他消费者


        Future<Void> future = executorService.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                long seq ;
                for (int i = 0; i < 10; i++) {
                    //获取sequence， 一个ringBuffer一个可用区块
                    seq = ringBuffer.next();
                    //给指定的sequence区块放入数据
                    ringBuffer.get(seq).setPrice(Math.random()*8888);
                    //发布这个数据，让所有消费者（Handle）可见
                    ringBuffer.publish(seq);
                }

                return null;
            }
        });

        //等待生产者结束
        future.get();
        Thread.sleep(1000);
        //通知事件处理器（或者说消息） 可以结束了，但不是马上就结束
        transProcessor.halt();
        //停止线程
        executorService.shutdown();
    }
}
