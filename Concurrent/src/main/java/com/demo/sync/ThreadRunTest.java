package com.demo.sync;

/**
 * 程序运行会进入死循环，为什么呢？
 *  jdk1.5之后对Thread进行了优化，对每一个Thread 内部开辟一块内存，
 *  当主内存中的变量值修改之后，thread内部是看不到的，读取的还是thread内部
 *  变量的副本
 */
public class ThreadRunTest extends Thread{

    public boolean isRunning = true;


    @Override
    public void run() {
        System.out.println("Thread running ....");
        while (isRunning){
            ////
        }
        System.out.println("Thread end ....");
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadRunTest runTest = new ThreadRunTest();
        runTest.start();
        Thread.currentThread().sleep(1000);
        runTest.isRunning = (false);
        System.out.println("设置 isRunning = false");
        Thread.currentThread().sleep(1000);
        System.out.println("isRunning="+ runTest.isRunning);
    }
}
