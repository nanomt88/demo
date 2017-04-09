package com.demo.concurrent.sync;

/**
 * 很显然，从这个测试例子中，写在方法上的synchronized关键字，锁的对象是 this.class，
 * 既一个类，只有方法能执行
 */
public class SyncSample2 {

    public synchronized void method1(){
        System.out.println("method1 invoke...");
        method2();
    }

    public synchronized void method2(){
        System.out.println("method2 invoke...");
        method3();
    }

    public synchronized void method3(){
        try {
            Thread.currentThread().sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("method3 invoke...");
    }


    public synchronized void method4(){
        System.out.println("method4 invoke...");
    }

    public static void main(String[] args) throws InterruptedException {

        final  SyncSample2 sample = new SyncSample2();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                sample.method1();
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                sample.method4();
            }
        });
        thread1.start();
        Thread.currentThread().sleep(1000);
        thread2.start();
    }
}
