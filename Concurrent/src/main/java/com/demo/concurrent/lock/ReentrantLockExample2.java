package com.demo.concurrent.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/7 下午7:52
 * @Description: //TODO
 */

public class ReentrantLockExample2 {

    ReentrantLock lock = new ReentrantLock();

    public void increment (int i){
        try {
            lock.lock();
            System.out.println("我是第"+ i +"个循环");
            if(i > 10){
                return;
            }else{
                increment(i+1);
            }
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        ReentrantLockExample2 a = new ReentrantLockExample2();
        a.increment(0);
    }
}
