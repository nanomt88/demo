package com.demo.concurrent.threadpool;

import java.util.concurrent.*;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/7 上午12:56
 * @Description: //TODO
 */

public class ThreadPoolExample {

    public static void main(String[] args) {

//        ExecutorService service = Executors.newCachedThreadPool();
//        Executors.newFixedThreadPool(10);
//        Executors.newSingleThreadExecutor();
//        Executors.newScheduledThreadPool(2);

//        ThreadPoolExecutor service = new ThreadPoolExecutor(2,4,
//                0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 100000; i++) {
            Thread s = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+" running ...");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            service.execute(s);
        }

    }

}
