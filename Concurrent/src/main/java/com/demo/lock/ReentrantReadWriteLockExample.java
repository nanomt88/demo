package com.demo.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/7 下午7:53
 * @Description:  读写锁，读读共享，读写互斥，写写互斥
 */

public class ReentrantReadWriteLockExample {

    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public void read(){
        try {
            readLock.lock();
            System.out.println(Thread.currentThread().getName() + " read method invoke ...");
            Thread.sleep(3000);
            System.out.println(Thread.currentThread().getName() + " read method finished ...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    public void write(){
        try {
            writeLock.lock();
            System.out.println(Thread.currentThread().getName() + " write method invoke ...");
            Thread.sleep(3000);
            System.out.println(Thread.currentThread().getName() + " write method finished ...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final ReentrantReadWriteLockExample r = new ReentrantReadWriteLockExample();
        Thread s1 = new Thread(new Runnable() {
            @Override
            public void run() {
                r.read();
            }
        },"t1");

        Thread s2 = new Thread(new Runnable() {
            @Override
            public void run() {
                r.read();
            }
        },"t2");

        s1.start();s2.start();

        Thread.currentThread().sleep(1000);

        Thread s3 = new Thread(new Runnable() {
            @Override
            public void run() {
                r.write();
            }
        },"t3");

        Thread s4 = new Thread(new Runnable() {
            @Override
            public void run() {
                r.write();
            }
        },"t4");
        Thread s5 = new Thread(new Runnable() {
            @Override
            public void run() {
                r.read();
            }
        },"t5");

        s3.start();s4.start();s5.start();
    }

}
