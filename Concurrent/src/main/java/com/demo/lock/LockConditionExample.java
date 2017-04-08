package com.demo.lock;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/8 下午9:48
 * @Description: //TODO
 */

public class LockConditionExample {

    public static void main(String[] args) {

        final ProductQueue<String> queue = new ProductQueue<>();

        Thread s = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 15; i++) {
                    System.out.println(Thread.currentThread().getName()+ "  添加元素：  Task"+i);
                    queue.put("Task"+i);
                }
            }
        },"t1");
        s.start();

        Thread s2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for ( ; ; ) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+ "  取走元素：  " + queue.take());
                }
            }
        },"t2");
        s2.start();

        Thread s3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for ( ; ; ) {
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+ "  取走元素：  " + queue.take());
                }
            }
        },"t3");
        s3.start();
    }

}
