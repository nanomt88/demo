package com.demo.helloworld;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by ZBOOK-17 on 2017/4/11.
 */
public class LongEventMain2 {

    public static void main(String[] args) {
        //创建工厂
        LongEventFactory factory = new LongEventFactory();
        //设置bufferSize，也就是RingBuffer大小，必须是2的N次方
        int bufferSize = 1024*1024;

        /**
         //BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现
         WaitStrategy BLOCKING_WAIT = new BlockingWaitStrategy();
         //SleepingWaitStrategy 的性能表现跟BlockingWaitStrategy差不多，对CPU的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景
         WaitStrategy SLEEPING_WAIT = new SleepingWaitStrategy();
         //YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于CPU逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性
         WaitStrategy YIELDING_WAIT = new YieldingWaitStrategy();
         */

        //创建disruptor
        //DaemonThreadFactory.INSTANCE
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, bufferSize,
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r);
                    }
                }, ProducerType.SINGLE, new YieldingWaitStrategy());
        //设置消费者事件监听方法
        disruptor.handleEventsWith(new LongEventConsumer());
        //启动
        disruptor.start();

        //Disruptor 的事件发布过程是一个两阶段提交的过程：
        //发布事件
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        LongEventProducer producer = new LongEventProducer(ringBuffer);
//        LongEventProducerWithTranslator producer = new LongEventProducerWithTranslator(ringBuffer);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8); //java中，long类型占用8个字节

        for(long l = 10000; l < 10100 ; l++){
            byteBuffer.putLong(0, l);
            producer.publish(byteBuffer);
        }

        //关闭 disruptor，方法会堵塞，直至所有的事件都得到处理；
        disruptor.shutdown();
        //关闭 disruptor 使用的线程池；如果需要的话，必须手动关闭， disruptor 在 shutdown 时不会自动关闭
    }
}
