package com.nanomt88.demo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/15 下午5:54
 * @Description: //TODO
 */

public class Test {

    public static void main(String[] args) {

        final AtomicInteger i = new AtomicInteger(0);

        new Thread(new Runnable() {
            public void run() {
                while (true){
                    while ( i.get() > 2){
                        LockSupport.parkNanos(1000*1000*1000);
                        System.out.println("park");
                    }
                    System.out.println("i="+i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i.incrementAndGet();
                }
            }
        }).start();

    }
}
