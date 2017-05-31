package com.demo.concurrent.lock;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by ZBOOK-17 on 2017/4/5.
 */
public class CyclicBarrierExample {

    public static void main(String[] args) {

        final Random random = new Random();

        final CyclicBarrier cb = new CyclicBarrier(3);

        System.out.println("所有选手准备好了吗？？？");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(random.nextInt(5) * 1000);
                    System.out.println(Thread.currentThread().getName() + " 我准备好啦..." );

                    cb.await();
                    System.out.println(Thread.currentThread().getName() + " 我开始跑啦啦啦..." );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(random.nextInt(5) * 1000);
                    System.out.println(Thread.currentThread().getName() + " 我准备好啦..." );

                    cb.await();
                    System.out.println(Thread.currentThread().getName() + " 我开始跑啦啦啦..." );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(random.nextInt(5) * 1000);
                    System.out.println(Thread.currentThread().getName() + " 我准备好啦..." );

                    cb.await();
                    System.out.println(Thread.currentThread().getName() + " 我开始跑啦啦啦..." );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
