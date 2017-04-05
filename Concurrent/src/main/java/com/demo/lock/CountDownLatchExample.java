package com.demo.lock;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ZBOOK-17 on 2017/4/5.
 */
public class CountDownLatchExample {

    public static void main(String[] args) {

        final Random random = new Random();

        final CountDownLatch countDownLatch = new CountDownLatch(3);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(random.nextInt(5)*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " 工作完成啦..." );
                countDownLatch.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(random.nextInt(5)*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " 工作完成啦..." );
                countDownLatch.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(random.nextInt(5)*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " 工作完成啦..." );
                countDownLatch.countDown();
            }
        }).start();

        try {
            System.out.println("等待各个任务执行完成");
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("所有认务都执行完成咯咯咯");
    }
}
