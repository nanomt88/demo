package com.demo.future;

import java.util.concurrent.*;
import java.util.concurrent.Callable;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 下午2:19
 * @Description: 使用 jdk自带的Future 实现future模式
 */

public class FutureExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTask<String> task = new FutureTask(new Callable<String>() {
            @Override
            public String call() throws Exception {
                //模拟真实的业务执行耗时操作
                Thread.currentThread().sleep(2000);

                return "哎呀妈呀，工作完了...";
            }
        });
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.submit(task);

        Thread.sleep(1500);
        System.out.println("其他工作完了，等待任务返回结果...");
        System.out.println(task.get());
        threadPool.shutdown();
    }

}
