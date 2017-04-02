package com.demo.notify;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/2 ����4:13
 * @Description:    ʹ��CountDownLatch ����������߳�ֱ��ͨ��
 */

public class ThreadNotifyByCountDownLatch {

    private volatile List<String> list = new ArrayList<>();

    public void add(){
        list.add("String");
    }

    public int size(){
        return list.size();
    }

    public static void main(String[] args) {
        //�߳�ͨ��
        final CountDownLatch latch = new CountDownLatch(1);

        final ThreadNotifyByWhile obj = new ThreadNotifyByWhile();

        Thread s1 = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 10; i++) {

                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    obj.add();
                    System.out.println("��ǰ�߳� " + Thread.currentThread().getName() + " ����µ�Ԫ��" + i);

                    if(i==5){
                        //����countDown֮������������̻߳�������
                        latch.countDown();
                    }

                }
            }
        });

        Thread s2 = new Thread(new Runnable() {
            @Override
            public void run() {
                if (obj.size() != 5) {

                    try {

                        //CountDownLatch.await()������������ȵ������̵߳���countDown���֮��Ż�ִ��
                        latch.await();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("��ǰ�߳� "+Thread.currentThread().getName() +" ���յ���Ϣ");
                    throw new RuntimeException();
                }
            }
        });

        s2.start();
        s1.start();

    }
}
