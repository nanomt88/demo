package com.demo.product;

import java.util.concurrent.BlockingQueue;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 下午3:06
 * @Description: //TODO
 */

public class ConsumerExample implements Runnable{

    private BlockingQueue<Data> blockingQueue = null;

    private boolean isRunning = true;

    public ConsumerExample(BlockingQueue<Data> blockingQueue){
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        while (isRunning){
            try {
                Data data = blockingQueue.take();
                Thread.sleep(500);
                System.out.println(Thread.currentThread().getName() + " 执行任务：" + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        isRunning = false;
    }
}
