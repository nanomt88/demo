package com.demo.concurrent.sync;

/**
 * synchronized 关键字相互套用： 一个简单的重入锁
 */
public class SyncSample {

    public synchronized void method1(){
        System.out.println("method1 invoke...");
        method2();
    }

    public synchronized void method2(){
        System.out.println("method2 invoke...");
        method3();
    }

    public synchronized void method3(){
        System.out.println("method3 invoke...");
    }

    public static void main(String[] args) {
        SyncSample sample = new SyncSample();
        sample.method1();
    }
}
