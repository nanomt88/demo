package com.demo.volatiles;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/2 ����11:44
 * @Description:  ʹ��volatile�����εı������ڶ�ȡ��ʱ��ӵ�и��ߵ�����
 *                ���synchronized�ؼ���֮�󣬿��Ա�֤�Ա����޸ĵ�ԭ���ԣ�ʹ�ö���д�ٵĳ���
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
