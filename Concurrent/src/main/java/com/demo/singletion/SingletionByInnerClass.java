package com.demo.singletion;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/2 ����5:45
 * @Description:   ���þ�̬�ڲ�������ԣ�дһ��������
 */

public class SingletionByInnerClass {

    private SingletionByInnerClass(){}

    public static SingletionByInnerClass getInstance(){
        return SingletionInner.instance;
    }

    private static class SingletionInner{
        private static SingletionByInnerClass instance = new SingletionByInnerClass();
    }

    public static void main(String[] args) {

        Thread s1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(SingletionByInnerClass.getInstance().hashCode());
            }
        });

        Thread s2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(SingletionByInnerClass.getInstance().hashCode());
            }
        });
        s1.start();
        s2.start();
    }
}
