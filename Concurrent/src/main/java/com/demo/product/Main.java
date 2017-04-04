package com.demo.product;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 下午3:05
 * @Description: //TODO
 */

public class Main {

    public static void main(String[] args) {
        //新建一个无界队列
        BlockingQueue<Data> queue = new LinkedBlockingDeque<>();
        //创建生产者
        ProviderExample p1 = new ProviderExample(queue);
        ProviderExample p2 = new ProviderExample(queue);
        ProviderExample p3 = new ProviderExample(queue);
        //消费者
        ConsumerExample c1 = new ConsumerExample(queue);
        ConsumerExample c2 = new ConsumerExample(queue);
        ConsumerExample c3 = new ConsumerExample(queue);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(p1);
        executorService.submit(p2);
        executorService.submit(p3);
        executorService.submit(c1);
        executorService.submit(c2);
        executorService.submit(c3);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        p1.stop();p2.stop();p3.stop();
        c1.stop();c2.stop();c3.stop();
        executorService.shutdown();

    }

}
