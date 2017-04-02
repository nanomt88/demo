package com.demo.singletion;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/2 ����5:45
 * @Description: //TODO
 */

public class SingletionByDoubleCheck {

    private static SingletionByDoubleCheck instance;

    private SingletionByDoubleCheck(){}

    public static SingletionByDoubleCheck getInstance(){
        if(instance == null){

            try {
                //�ӳٶ����ʼ��ʱ�䣬ģ����̲߳������ʵ������ ����ĸ���
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (SingletionByDoubleCheck.class){
                //�ڲ���Ҫ�ټ��һ�Σ���ֹ�������ʳ���
                if(instance == null){
                    instance = new SingletionByDoubleCheck();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {

        Thread s1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(SingletionByDoubleCheck.getInstance().hashCode());
            }
        });

        Thread s2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(SingletionByDoubleCheck.getInstance().hashCode());
            }
        });
        s1.start();
        s2.start();
    }
}
