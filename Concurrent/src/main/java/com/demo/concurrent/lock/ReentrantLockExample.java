package com.demo.concurrent.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
            System.out.println(Thread.currentThread().getName() + " : method1 condition1 等待");
            Thread.sleep(1000);
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
            System.out.println(Thread.currentThread().getName() + " : method2 condition2 等待");
            Thread.sleep(1000);
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
            System.out.println(Thread.currentThread().getName() + " : method3 condition1 等待");
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
            System.out.println(Thread.currentThread().getName() + " : condition1 唤醒 method1 和 method3");
            Thread.sleep(2000);
            condition1.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void method5(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " : condition2 唤醒 method2");
            Thread.sleep(2000);
            condition2.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

        //t1先启动，t1先拿到锁，t1执行完成之后 t1 condition1进入等待，然后释放锁t1进入等待阶段
        //t2拿到锁，t2执行后，t2 condition进入等待，释放锁 t2进入等待阶段
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
        //
        t6.start();

        Thread.sleep(4000);
        //t4 拿到锁后，唤醒condition1 的线程，t1、t3继续执行，执行完成之后释放锁
        t4.start();
        Thread.sleep(2000);
        //t5 拿到锁后，唤醒condition2 的线程，t2继续执行
        t5.start();
    }
}
