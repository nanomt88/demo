package com.demo.future;

import java.util.concurrent.*;
import java.util.concurrent.Callable;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 ����2:19
 * @Description: ʹ�� jdk�Դ���Future ʵ��futureģʽ
 */

public class FutureExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTask<String> task = new FutureTask(new Callable<String>() {
            @Override
            public String call() throws Exception {
                //ģ����ʵ��ҵ��ִ�к�ʱ����
                Thread.currentThread().sleep(2000);

                return "��ѽ��ѽ����������...";
            }
        });
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.submit(task);

        Thread.sleep(1500);
        System.out.println("�����������ˣ��ȴ����񷵻ؽ��...");
        System.out.println(task.get());
        threadPool.shutdown();
    }

}
