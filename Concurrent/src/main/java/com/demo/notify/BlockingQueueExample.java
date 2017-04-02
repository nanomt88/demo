package com.demo.notify;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/2 ����4:27
 * @Description:  �Լ�дһ���򵥵� LinkedBlockingQueue С����
 */

public class BlockingQueueExample  {
    /**
     * ��������
     */
    private LinkedList<Object> list = new LinkedList<>();
    //��
    private Object lock = new Object();
    //��С
    private final AtomicInteger size = new AtomicInteger();
    //�������ֵ
    private int maxSize = 16;

    public BlockingQueueExample(){  }

    public BlockingQueueExample(int maxSize){
        this.maxSize = maxSize;
    }

    public void putTask(Object object){
        synchronized (lock) {
            //������ȳ������ֵ���������ֱ����Ԫ�ر��Ƴ�Ϊֹ
            while(size.get() >= maxSize){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //throw new RuntimeException("Task refused by max limit");
            }
            //�������
            list.add(object);
            //��������1
            size.incrementAndGet();
            //����������֮����Ҫ֪ͨ���е��߳�
            lock.notifyAll();
        }
    }

    public Object popTask(){
        synchronized (lock){
            //�������Ϊ�գ��������ֱ����Ԫ�ر���ӽ���Ϊֹ
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
                System.out.println(Thread.currentThread().getName()+" ����Ԫ�أ�a");
                queue.putTask("b");
                System.out.println(Thread.currentThread().getName()+" ����Ԫ�أ�b");
                queue.putTask("c");
                System.out.println(Thread.currentThread().getName()+" ����Ԫ�أ�c");
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
                System.out.println(Thread.currentThread().getName()+" ȡ��Ԫ�أ�"+a);
                Object a2 = queue.popTask();
                System.out.println(Thread.currentThread().getName()+" ȡ��Ԫ�أ�"+a2);
            }
        });

        Thread s3 = new Thread(new Runnable() {
            @Override
            public void run() {
                queue.putTask("m");
                System.out.println(Thread.currentThread().getName()+" ����Ԫ�أ�m");
                queue.putTask("l");
                System.out.println(Thread.currentThread().getName()+" ����Ԫ�أ�l");
                Object a2 = queue.popTask();
                System.out.println(Thread.currentThread().getName()+" ȡ��Ԫ�أ�"+a2);
            }
        });

        s2.start();

        s3.start();

    }
}
