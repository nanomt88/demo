package com.demo.notify;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/2 下午4:13
 * @Description:    使用CountDownLatch 工具类进行线程直接通信
 */

public class ThreadNotifyByCountDownLatch {

    private volatile List<String> list = new ArrayList<>();

    public void add(){
        list.add("String");
    }

    public int size(){
        return list.size();
    }

    public static void main(String[] args) {
        //线程通信
        final CountDownLatch latch = new CountDownLatch(1);

        final ThreadNotifyByWhile obj = new ThreadNotifyByWhile();

        Thread s1 = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 10; i++) {

                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    obj.add();
                    System.out.println("当前线程 " + Thread.currentThread().getName() + " 添加新的元素" + i);

                    if(i==5){
                        //调用countDown之后，另外堵塞的线程会立马唤醒
                        latch.countDown();
                    }

                }
            }
        });

        Thread s2 = new Thread(new Runnable() {
            @Override
            public void run() {
                if (obj.size() != 5) {

                    try {

                        //CountDownLatch.await()方法会堵塞，等到其他线程调用countDown完毕之后才会执行
                        latch.await();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("当前线程 "+Thread.currentThread().getName() +" 接收到信息");
                    throw new RuntimeException();
                }
            }
        });

        s2.start();
        s1.start();

    }
}
