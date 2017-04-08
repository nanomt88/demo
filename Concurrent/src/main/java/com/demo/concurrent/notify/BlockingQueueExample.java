package com.demo.concurrent.notify;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/2 下午4:27
 * @Description:  自己写一个简单的 LinkedBlockingQueue 小例子
 */

public class BlockingQueueExample  {
    /**
     * 队列链表
     */
    private LinkedList<Object> list = new LinkedList<>();
    //锁
    private Object lock = new Object();
    //大小
    private final AtomicInteger size = new AtomicInteger();
    //队列最大值
    private int maxSize = 16;

    public BlockingQueueExample(){  }

    public BlockingQueueExample(int maxSize){
        this.maxSize = maxSize;
    }

    public void putTask(Object object){
        synchronized (lock) {
            //如果长度超过最大值，则堵塞，直到有元素被移除为止
            while(size.get() >= maxSize){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //throw new RuntimeException("Task refused by max limit");
            }
            //添加任务
            list.add(object);
            //计数器加1
            size.incrementAndGet();
            //添加任务完成之后，需要通知所有的线程
            lock.notifyAll();
        }
    }

    public Object popTask(){
        synchronized (lock){
            //如果长度为空，则堵塞，直到有元素被添加进来为止
            while (size.get() == 0){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Object o = list.removeFirst();
            size.decrementAndGet();
            lock.notifyAll();
            return o;
        }
    }

    public int size(){
        synchronized (lock){
            return size.get();
        }
    }


    public static void main(String[] args) {

        final BlockingQueueExample queue = new BlockingQueueExample();

        Thread s = new Thread(new Runnable() {
            @Override
            public void run() {
                queue.putTask("a");
                System.out.println(Thread.currentThread().getName()+" 放入元素：a");
                queue.putTask("b");
                System.out.println(Thread.currentThread().getName()+" 放入元素：b");
                queue.putTask("c");
                System.out.println(Thread.currentThread().getName()+" 放入元素：c");
            }
        });
        s.start();

        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread s2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Object a = queue.popTask();
                System.out.println(Thread.currentThread().getName()+" 取出元素："+a);
                Object a2 = queue.popTask();
                System.out.println(Thread.currentThread().getName()+" 取出元素："+a2);
            }
        });

        Thread s3 = new Thread(new Runnable() {
            @Override
            public void run() {
                queue.putTask("m");
                System.out.println(Thread.currentThread().getName()+" 放入元素：m");
                queue.putTask("l");
                System.out.println(Thread.currentThread().getName()+" 放入元素：l");
                Object a2 = queue.popTask();
                System.out.println(Thread.currentThread().getName()+" 取出元素："+a2);
            }
        });

        s2.start();

        s3.start();

    }
}
