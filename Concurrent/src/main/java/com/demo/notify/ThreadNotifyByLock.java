package com.demo.notify;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/2 下午4:06
 * @Description: 当调用 Object.wait()方法的时候，会释放当前的锁；调用Object.notify()并不会释放当前的锁
 *  等到当前线程执行完毕后才会释放锁
 */

public class ThreadNotifyByLock {

    private volatile List<String> list = new ArrayList<>();

    public void add(){
        list.add("String");
    }

    public int size(){
        return list.size();
    }

    public static void main(String[] args) {

        final Object lock = new Object();

        final ThreadNotifyByWhile obj = new ThreadNotifyByWhile();

        Thread s1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){
                for (int i = 0; i < 10; i++) {

                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    obj.add();
                    System.out.println("当前线程 " + Thread.currentThread().getName() + " 添加新的元素" + i);

                    if(i==5){
                        //wait 会释放当前的锁，但是notify不会释放当前的锁
                        lock.notify();
                    }

                }
            }
            }
        });

        Thread s2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){
                    if (obj.size() != 5) {

                        try {
                            //wait 会释放当前的锁，但是notify不会释放当前的锁
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println("当前线程 "+Thread.currentThread().getName() +" 接收到信息");
                        throw new RuntimeException();
                    }
                }
            }
        });

        s2.start();
        s1.start();

    }
}
