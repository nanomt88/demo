package com.demo.concurrent.product;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 下午3:05
 * @Description: //TODO
 */

public class ProviderExample implements Runnable{

    private BlockingQueue<Data> blockingQueue = null;

    private boolean isRunning = true;

    public ProviderExample(BlockingQueue<Data> blockingQueue){
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run(){

        while (isRunning){
            Random random = new Random();

            try {
                //模拟 业务执行时间
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Data data = new Data();
            data.setId(random.nextInt(10000));
            data.setName("任务"+data.getId());

            try {
                System.out.println(Thread.currentThread().getName()+" 添加任务："+data);
                blockingQueue.offer(data,2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("添加任务失败啊啊啊..." + data) ;
            }
        }

    }

    public void stop(){
        isRunning = false;
    }
}
