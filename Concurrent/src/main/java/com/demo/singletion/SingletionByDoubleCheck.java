package com.demo.singletion;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/2 下午5:45
 * @Description: //TODO
 */

public class SingletionByDoubleCheck {

    private static SingletionByDoubleCheck instance;

    private SingletionByDoubleCheck(){}

    public static SingletionByDoubleCheck getInstance(){
        if(instance == null){

            try {
                //延迟对象初始化时间，模拟多线程并发访问的情况下 出错的概率
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (SingletionByDoubleCheck.class){
                //内部需要再检查一次，防止并发访问出错
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
