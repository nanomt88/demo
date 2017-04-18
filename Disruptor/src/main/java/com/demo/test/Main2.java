package com.demo.test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/14 下午11:10
 * @Description: //TODO
 */

public class Main2 {

    public static void main(String[] args) {

        final Main2  m = new Main2();
        final Random random = new Random();

        long start = System.currentTimeMillis();

        CountDownLatch latch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    TestEvent event = new TestEvent();
                    event.setPrice(random.nextDouble() * 9999);

                    m.hand1(event);
                    m.hand2(event);
                    m.hand3(event);
                    latch.countDown();
                }
            }).start();
        }
        try {
            latch.await();
            System.out.println("总耗时："+  (System.currentTimeMillis()-start) );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void hand1(TestEvent event){
        System.out.println(Thread.currentThread().getName()+"  Handle1 : set name");
        event.setName("name"+10);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void hand2(TestEvent event){
        System.out.println(Thread.currentThread().getName()+"  Handle2 : set price");
        event.setPrice(100*999);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void hand3(TestEvent event) {
        System.out.println("Handle3 : "+event.toString());
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

