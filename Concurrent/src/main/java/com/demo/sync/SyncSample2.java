package com.demo.sync;

/**
 * synchronized 关键字相互套用： 一个简单的重入锁
 */
public class SyncSample2 {
    private Object lock = new Object();
    public void method1(){
        synchronized (lock) {
            System.out.println("method1 invoke...");
            method2();
        }
    }

    public void method2(){
        synchronized (lock) {
            System.out.println("method2 invoke...");
            method3();
        }
    }

    public void method3(){
        synchronized (lock) {
            System.out.println("method3 invoke...");
        }
    }

    public static void main(String[] args) {
        SyncSample2 sample = new SyncSample2();
        sample.method1();
    }
}
