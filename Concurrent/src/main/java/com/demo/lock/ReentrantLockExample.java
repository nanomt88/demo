package com.demo.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/7 下午7:52
 * @Description:
 *
 *      当在使用多个 Condition 条件的时候，必须保证唤醒的时候，Condition条件有线程在await
 *      在多个条件完全满足的情况下 才能获取到锁
 *
 */

public class ReentrantLockExample {
    ReentrantLock lock = new ReentrantLock();

    Condition condition1 = lock.newCondition();
    Condition condition2 = lock.newCondition();

    public void method1(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " : method1 进入");
            condition1.await();
            System.out.println(Thread.currentThread().getName() + " : method1 退出");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void method2(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " : method2 进入");
            condition2.await();
            System.out.println(Thread.currentThread().getName() + " : method2 退出");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void method3(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " : method3 进入");
            condition1.await();
            System.out.println(Thread.currentThread().getName() + " : method3 退出");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void method4(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " : 唤醒 method1 和 method3");
            condition1.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void method5(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " : 唤醒 method2");
            condition2.signal();
        } finally {
            lock.unlock();
        }
    }

    public void method6(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " : method6 拿到锁");
        } finally {
            lock.unlock();
        }
    }
    public static void main(String[] args) throws InterruptedException {

        final ReentrantLockExample r = new ReentrantLockExample();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                r.method1();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                r.method2();
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                r.method3();
            }
        });

        t1.start();t2.start();t3.start();

        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                r.method4();
            }
        });

        Thread t5 = new Thread(new Runnable() {
            @Override
            public void run() {
                r.method5();
            }
        });

        Thread t6 = new Thread(new Runnable() {
            @Override
            public void run() {
                r.method6();
            }
        });
        t6.start();

        Thread.sleep(2000);
        t4.start();
        Thread.sleep(2000);
        t5.start();
    }
}
