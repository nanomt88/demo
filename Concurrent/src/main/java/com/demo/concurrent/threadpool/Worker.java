package com.demo.concurrent.threadpool;

import org.apache.log4j.Logger;

/**
 * @Copyright 		(c) Lictecs
 * @author 			xudong.hong@lictecs.com
 * @Date 			2012-10-30
 * @Description: 
 *					工作线程类
 * @param <T>
 */
public class Worker<T extends Callable> extends Thread {
    /** 是否激活状态   */
	private volatile boolean isActive = false;
	/** 线程池 */
	private WorkerPool<T> threadPool = null;
	/** 任务执行时缓冲区  */
	private TaskBuffer<T> taskBuffer = null;
	/** 任务执行者 */
	private Executor<T> executor = null;
	/** 任务每次执行粒度 */
	private int bulkSize = 1;
	/** log4j 实例 */
	private Logger logger = Logger.getLogger(Worker.class);
	
	/**
	 * 构造函数
	 * @param taskName 队列名称
	 * @param threadPool  线程池
	 * @param bulkSize 工作线程数
	 * @param taskBuffer 
	 */
	public Worker(String taskName, WorkerPool<T> threadPool,
	        int bulkSize, TaskBuffer<T> taskBuffer) {
		this(taskName, threadPool, bulkSize, taskBuffer, null);
	}
	/**
	 * 
	 * @param taskName 工作线程名称
	 * @param threadPool 线程池
	 * @param bulkSize 每次从队列读取的任务数
	 * @param taskBuffer 任务缓冲
	 * @param executor 执行者
	 */
	public Worker(String taskName, WorkerPool<T> threadPool, int bulkSize,
	        TaskBuffer<T> taskBuffer, Executor<T> executor) {
	    if (taskName == null) {
	        throw new IllegalArgumentException("taskName is null.");
	    }
	    if (threadPool == null) {
	        throw new IllegalArgumentException("threadPool is null");
	    }
	    if (bulkSize <= 0) {
            throw new IllegalArgumentException("bulkSize is invalid.");
        }
	    if (taskBuffer == null) {
	        throw new IllegalArgumentException("taskBuffer is null");
	    }
	    setName(taskName);
	    this.threadPool = threadPool;
	    this.taskBuffer = taskBuffer;
	    this.isActive = true;
	    this.bulkSize = bulkSize;
	    if (executor == null) 
	        this.executor = createDefaultExecutor();
	    else
	        this.executor = executor;
	}
	
	/**
	 * 线程主循环，从队列提取任务，然后调用执行器执行任务
	 */
	@Override
	public void run() {
		while(isActive) {
			try {
                if(threadPool.getNextTask(taskBuffer, bulkSize)){
                	logger.debug("-------------------------------------");
                	logger.debug(getName()+ " deque taskBuffer size: "
                			+ taskBuffer.getTaskCount());
                	logger.debug(getName()+ " : Begin to execute task ...");
                	taskBuffer.execute(executor);
                	taskBuffer.clear();
                	logger.debug(getName()+ " : end.");
                	logger.debug("-------------------------------------");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
		} 
	}
	
	/**
	 * 停止线程执行
	 */
	public void terminate() {
		isActive = false;
	}
	
	/**
	 * 创建缺省的任务执行器
	 * @return
	 */
	private Executor<T> createDefaultExecutor() {
		DefaultExecutorListener execLsnr = new DefaultExecutorListener();
		Executor<T> lExecutor = new DefaultExecutor<T>(execLsnr);
		return lExecutor;
	}
}
