package com.demo.notify;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/2 ����4:06
 * @Description: ������ Object.wait()������ʱ�򣬻��ͷŵ�ǰ����������Object.notify()�������ͷŵ�ǰ����
 *  �ȵ���ǰ�߳�ִ����Ϻ�Ż��ͷ���
 */

public class ThreadNotifyByLock {

    private volatile List<String> list = new ArrayList<>();

    public void add(){
        list.add("String");
    }

    public int size(){
        return list.size();
    }

    public static void main(String[] args) {

        final Object lock = new Object();

        final ThreadNotifyByWhile obj = new ThreadNotifyByWhile();

        Thread s1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){
                for (int i = 0; i < 10; i++) {

                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    obj.add();
                    System.out.println("��ǰ�߳� " + Thread.currentThread().getName() + " ����µ�Ԫ��" + i);

                    if(i==5){
                        //wait ���ͷŵ�ǰ����������notify�����ͷŵ�ǰ����
                        lock.notify();
                    }

                }
            }
            }
        });

        Thread s2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){
                    if (obj.size() != 5) {

                        try {
                            //wait ���ͷŵ�ǰ����������notify�����ͷŵ�ǰ����
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println("��ǰ�߳� "+Thread.currentThread().getName() +" ���յ���Ϣ");
                        throw new RuntimeException();
                    }
                }
            }
        });

        s2.start();
        s1.start();

    }
}
