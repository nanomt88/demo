package com.demo.notify;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/2 下午3:51
 * @Description: //TODO
 */

public class ThreadNotifyByWhile {

    private volatile List<String> list = new ArrayList<>();

    public void add(){
        list.add("String");
    }

    public int size(){
        return list.size();
    }

    public static void main(String[] args) {

        final ThreadNotifyByWhile obj = new ThreadNotifyByWhile();

        Thread s1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<10; i++){

                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    obj.add();
                    System.out.println("当前线程 "+Thread.currentThread().getName() +" 添加新的元素"+i);
                }
            }
        });

        Thread s2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (obj.size() == 5) {
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
