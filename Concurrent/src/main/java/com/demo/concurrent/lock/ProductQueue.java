package com.demo.concurrent.lock;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/4/8 下午9:49
 * @Description:  一个使用 ReentrantLock、Condition 实现的简单FIFO 堵塞队列
 */

public class ProductQueue<T> {
    //重入锁
    private ReentrantLock lock = new ReentrantLock();
    //当队列中满了的时候，该条件成立，等待其他线程取数据
    private Condition notFull = lock.newCondition();
    //当队列中空了的时候，该条件成立，等待其他线程存放数据
    private Condition notEmpty = lock.newCondition();

    private LinkedList<T> list = new LinkedList<T>();

    private volatile int count;
    private int max;


    public ProductQueue(){
        max = 10;
    }

    public ProductQueue(int max){
        this.max = max;
    }

    public void put(T t){
        try {
            //获取锁
            lock.lock();

            //如果队列满了，则等待，直到有其他线程调用take方法移除元素，队列有空闲空间之后，才能唤醒当前线程继续执行
            while (count >= max)
                notFull.await();

            count++;
            //添加任务到最后
            list.addLast(t);
            //唤醒其他等待的线程：当队列为空的时候，调用take的线程会堵塞住，直到这里调用notEmpty条件唤醒
            notEmpty.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public T take(){
        T o = null;
        try{
            //获取锁
            lock.lock();

            //如果队列为空，则等待，直到有其他线程调用put方法添加元素之后，才能唤醒当前线程继续执行
            while (count==0)
                notEmpty.await();
            count--;
            //取出队列第一个元素 并且返回
            o = list.removeFirst();

            //唤醒其他等待的线程： 当队列满的时候，调用put方法的线程就会堵塞住，直到这里调用notFull条件唤醒
            notFull.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return o;
    }

    public int size(){
        return count;
    }
}
