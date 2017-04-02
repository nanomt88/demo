package com.demo.sync;

/**
 * synchronized 关键字相互套用： 一个简单的重入锁
 */
public class SyncSample3 {
    public int i=10;
    public synchronized void method1(){
        i--;
        System.out.println("Super class , i=" + i );
    }

    static class SubSyncSample3 extends SyncSample3{
        public synchronized void method2(){
            while (i>0) {
                i--;
                System.out.println("Sub class , i=" + i);
                method1();
            }
        }
    }

    public static void main(String[] args) {
        Thread s = new Thread(new Runnable() {
            @Override
            public void run() {
                SubSyncSample3 s = new SubSyncSample3();
                s.method2();
            }
        });
        s.start();
    }
}
