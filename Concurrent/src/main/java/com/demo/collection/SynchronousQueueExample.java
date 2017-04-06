package com.demo.collection;

import java.util.concurrent.SynchronousQueue;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/6 下午10:06
 * @Description: //TODO
 */

public class SynchronousQueueExample {
    public static void main(String[] args) throws InterruptedException {

        final SynchronousQueue<String> queue = new SynchronousQueue();

        // 不能执行add操作，会提示 Queue full异常，
//        queue.add("aaa");
        //在执行put操作的时候 会堵塞，直到另一个线程执行take的时候，才能成功执行
//        queue.put("aaa");
        //当没有另一个线程执行put操作的时候，执行take操作会 堵塞线程
//        String a = queue.take();


        ///////////     以下是正确使用方式    ///////////////

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<3; i++) {
                    try {
                        Thread.sleep(2000);
                        System.out.println(Thread.currentThread().getName()+"放入元素：a"+i);
                        queue.put("a"+i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<3; i++) {
                    try {
                        Thread.sleep(1000);
                        String key = queue.take();
                        System.out.println(Thread.currentThread().getName()+"获取到数据" + key);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

}
