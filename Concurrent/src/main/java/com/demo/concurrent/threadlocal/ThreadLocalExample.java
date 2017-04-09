package com.demo.concurrent.threadlocal;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/2 下午5:38
 * @Description: //TODO
 */

public class ThreadLocalExample {

    private ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public void setName(String name){
        threadLocal.set(name);
    }

    public String getName(){
        return threadLocal.get();
    }

    public static void main(String[] args) {
        final ThreadLocalExample example = new ThreadLocalExample();
        Thread s = new Thread(new Runnable() {
            @Override
            public void run() {
                example.setName("Name 1");
                String name = example.getName();
                System.out.println(Thread.currentThread().getName()+" 获取名称："+name);
            }
        });
        s.start();
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread s2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String name = example.getName();
                System.out.println(Thread.currentThread().getName()+" 获取名称："+name);
            }
        });
        s2.start();

    }

}
