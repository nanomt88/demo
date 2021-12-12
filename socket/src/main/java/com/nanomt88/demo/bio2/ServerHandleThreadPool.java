package com.nanomt88.demo.bio2;

import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/4/15 下午3:59
 * @Description: //TODO
 */

public class ServerHandleThreadPool {

    private ExecutorService executorService;
    public ServerHandleThreadPool(int maxSize, int queueSize){
        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                maxSize, 120, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));

    }

    public void execute(Runnable runnable){
        this.executorService.execute(runnable);
    }
}
