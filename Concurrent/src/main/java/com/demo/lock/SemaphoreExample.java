package com.demo.lock;

import java.util.concurrent.Semaphore;

/**
 * Created by ZBOOK-17 on 2017/4/6.
 */
public class SemaphoreExample {

    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(4);

        for (int i = 0; i < 20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //获取许可
                        semaphore.acquire();

                        Thread.sleep(2000);
                    } catch (Inter ruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+" 执行完毕...");
                    //执行完成之后，释放许可
                    semaphore.release();
                }
            }).start();
        }

        System.out.println("到我了吗？？？");
    }

}
