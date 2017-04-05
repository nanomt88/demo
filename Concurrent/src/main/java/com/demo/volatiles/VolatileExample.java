package com.demo.volatiles;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/2 上午11:44
 * @Description:  使用volatile的修饰的变量，在读取的时候拥有更高的性能
 *                配合synchronized关键字之后，可以保证对变量修改的原子性，使用读多写少的场景
 */

public class VolatileExample {

    private volatile int i = 0;

    public int getValue(){
        return i;
    }

    public synchronized void plus(){
        i++;
    }

    public static void main(String[] args) {
        final VolatileExample v = new VolatileExample();

        Thread s1= new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<10000;i++) {
                    v.plus();
                    System.out.println(v.getValue());
                }
            }
        });

        Thread s2= new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<10000;i++) {
                    v.plus();
                    System.out.println(v.getValue());
                }
            }
        });

        s1.start();
        s2.start();
    }
}
