package com.demo.threadpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @Copyright 		(c) Lictecs
 * @author 			xudong.hong@lictecs.com
 * @Date 			2012-10-30
 * @Description: 
 *		线程池，默认创建default_queueSize(3)个队列，依次存放
 *      高(>5)、中(=5)、低任务(<5)，优先级越高越先执行；
 *      当队列中任务数量小于 每次执行的任务粒度时，将最后的任务全部执行
 *      后让每个线程sleep一段时间后再检查队列是否有新的任务进来
 *  
 *      待优化   
 *          1.添加计数器直接记录队列数量
 *          2.实现一个可以动态改变线程数的线程池
 * @param <T>
 */
public class WorkerPool<T extends Callable>{
    /** 线程池状态    */
    private static final int WORKER_POOL_STATE_NOT_STARTED = 0;
    private static final int WORKER_POOL_STATE_STARTED = 1;
    private static final int WORKER_POOL_STATE_STOPPED = 2;
    
    /** 默认队列数量，3个，分别存放高(>5)、中(=5)、低(<5) */
    private static int DEFAULT_QUEUESIZE = 3;
    /** 队列为空时，线程等待时间   */
    private static int THREAD_SLEEP_TIME = 1000;
    
    /**低优先级   */
    private final static int HIGH_PRIORITY = 0;
    /**中优先级   */
    private final static int MED_PRIORITY = 1;
    /**高优先级   */
    private final static int LOW_PRIORITY = 2;
    
    /** 线程池状态    */
    private int workerPoolState = WORKER_POOL_STATE_NOT_STARTED;
    /** 线程池核心线程数量   */
    private int workThreadSize ;
    /** 工作线程每次读入任务个数 */
    private int workerBulkSize ;
    /** 线程池中工作线程 缓存  */
    private List<Worker<T>> workers = null;
    /**  线程池中任务队列缓存 */
    private Map<Integer,TaskQueue<T>> queues = null;
    /** 检查有没有任务，任务锁    */
    private Object nextTaskLock = new Object();
    /** log4j 实例 */
    private Logger logger = Logger.getLogger(WorkerPool.class);
   
    /**
     * 缺省构造函数
     */
    public WorkerPool(){  
        //读取系统线程池配置参数
        workThreadSize = WorkerPoolParamsFactory.getWorkThreadSize();
        workerBulkSize = WorkerPoolParamsFactory.getWorkerBulkSize();
    	//池工作状态
        workerPoolState = WORKER_POOL_STATE_NOT_STARTED;
        //任务队列
        queues = initTaskQueueArray();
        //工作线程
        workers = initWorkerArray("ThreadPool", new DefaultTaskBufferFactory(), null);
        //直接启动
        start();
    }
    
    /**
     * 构造函数
     * @param threadPoolName     	线程池名称
     * @param workThreadSize    核心工作线程数
     * @param workerBulkSize     	线程执行任务粒度
     */
    public WorkerPool(String threadPoolName, int workThreadSize, 
            int workerBulkSize) {
        this(threadPoolName, workThreadSize, workerBulkSize, null, null);
    }
    
    /**
     * 构造函数
     * @param threadPoolName     线程池名称
     * @param workThreadSize    核心工作线程数
     * @param workerBulkSize     	线程执行任务粒度
     * @param taskBufferFactory  	自定义线程缓存容器
     * @param executor  
     */
    public WorkerPool(String threadPoolName, int workThreadSize,
            int workerBulkSize, TaskBufferFactory taskBufferFactory, 
            Executor<T> executor) {
        if (threadPoolName == null) {
            throw new IllegalArgumentException("threadPoolName is null");
        }
        if (workThreadSize <= 0) {
            throw new IllegalArgumentException("workThreadSize is invalid");
        }
        if (workerBulkSize <= 0) {
            throw new IllegalArgumentException("WorkerBulkSize is invalid");
        }
        // 工作状态
        this.workerPoolState = WORKER_POOL_STATE_NOT_STARTED;
        this.workThreadSize = workThreadSize;
        this.workerBulkSize = workerBulkSize;
        // 任务队列
        queues = initTaskQueueArray();
        // 工作线程池数量
        TaskBufferFactory _taskBufferFactory = null;
        if (taskBufferFactory == null) {
            _taskBufferFactory = new DefaultTaskBufferFactory();
        }else
            _taskBufferFactory = taskBufferFactory;
        workers = initWorkerArray(threadPoolName, _taskBufferFactory, executor);
    }
    
    /**
     * 启动线程池
     */
    public synchronized void start() {
        if (workerPoolState == WORKER_POOL_STATE_STARTED) {
        	logger.info(" Worker Pool has been running...");
            return;
        }
        if (workerPoolState == WORKER_POOL_STATE_STOPPED) {
            throw new IllegalStateException("Cannot restart Worker Pool");
        }
        for (int i = 0; i < workThreadSize; i++) {
            Worker<T> lWorker = workers.get(i);
            lWorker.start();
            logger.info(lWorker.getName()+" started...");
        }
        workerPoolState = WORKER_POOL_STATE_STARTED;
    }
    
    /**
     * 停止线程池
     */
    public synchronized void stop() {
        if (workerPoolState == WORKER_POOL_STATE_STOPPED) {
        	logger.info(" Worker Pool has stopped.");
            return;
        }
        if (workerPoolState == WORKER_POOL_STATE_NOT_STARTED) {
            throw new IllegalStateException("Worker Pool does not start!");
        }
        for (int i = 0; i < workThreadSize; i++) {
            Worker<T> lWorker = workers.get(i);
            lWorker.terminate();
            logger.info(lWorker.getName()+" terminate.");
        }
        workerPoolState = WORKER_POOL_STATE_STOPPED;
    }
    
    /**
     * 向线程池添加新任务
     * @param task
     */
    public void submit(T task) {
        if (workerPoolState != WORKER_POOL_STATE_STARTED) {
            throw new IllegalStateException("Worker Pool not start, abort!");
        }
        if (task == null) {
            throw new IllegalArgumentException("task is null.");
        }
        //读取任务优先级，
        int priority = AbstractCallable.NORM_PRIORITY;
        if(task instanceof AbstractCallable)
            priority= ((AbstractCallable) task).getPriority();
        //根据优先级分配任务到对应优先级队列中
        TaskQueue<T> lQueue = null;
        if(priority > AbstractCallable.NORM_PRIORITY){
            lQueue = queues.get(HIGH_PRIORITY);
        }else if(priority < AbstractCallable.NORM_PRIORITY){
            lQueue = queues.get(LOW_PRIORITY);
        }else{
        	lQueue = queues.get(MED_PRIORITY);
        }
        //
        synchronized (nextTaskLock) {
            lQueue.enque(task);
            nextTaskLock.notifyAll();
        }
    }
    /**
     * 提交一组任务
     * @param taskList
     */
    public void submit(List<T> taskList) {
        if (workerPoolState != WORKER_POOL_STATE_STARTED) {
            throw new IllegalStateException("Worker Pool not start");
        }
        if (taskList == null) {
            throw new IllegalArgumentException("Tasks is null");
        }
        int lTaskCount = taskList.size();
        for (int i = 0; i < lTaskCount; i++) {
            T lTask =  taskList.get(i);
            submit(lTask);
        }
    }
    /**
     * 获取线程池中所有任务数量
     * @return
     */
    public int getTaskCount() {
        int lTaskCount = 0;
        //锁住队列，不让添加，统计队列数量
        synchronized (nextTaskLock) {
            for (int i = 0; i < queues.size(); i++) {
                TaskQueue<T> lQueue = queues.get(i);
                lTaskCount += lQueue.getSize();
            }
            nextTaskLock.notifyAll();
        }
        return lTaskCount;
    }
    /**
     * 从线程池中获取队列中的任务并执行
     * @param aInTaskBuffer
     * @param aInTaskCount
     * @return
     * @throws Exception 
     */
    public boolean getNextTask(TaskBuffer<T> aInTaskBuffer, int aInTaskCount)
    		throws Exception {
        if (aInTaskBuffer == null) {
            throw new IllegalArgumentException("aInTaskBuffer is null");
        }
        if (aInTaskCount <= 0) {
            throw new IllegalArgumentException("aInTaskCount is invalid");
        }
        synchronized (nextTaskLock) {
            //统计队列中的等待执行的任务数量
            int lTaskCount = 0;
            for (int i = 0; i < queues.size(); i++) {
                TaskQueue<T> lQueue = queues.get(i);
                lTaskCount += lQueue.getSize();
            }
            //如果队列中任务不足 则等待1秒
            if(lTaskCount <= 0){
                try {
                	logger.debug(Thread.currentThread().getName()
                    	+ ": task queue is hunger, thread sleep 1s...");
                    nextTaskLock.wait(THREAD_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(lTaskCount < aInTaskCount){
                moveTaskToBuffer(aInTaskBuffer, lTaskCount);
                nextTaskLock.notifyAll();
                return true;
            }else{
                moveTaskToBuffer(aInTaskBuffer, aInTaskCount);
                nextTaskLock.notifyAll();
                return true;
            }
        }
        return false;
    }
    /**
     * 从队列中取任务并缓存到每个工作线程对应的缓冲区
     * @param taskBuffer
     * @param popTaskCount
     * @throws Exception
     */
    private void moveTaskToBuffer(TaskBuffer<T> taskBuffer, 
    		int popTaskCount) throws Exception{
    	//队列中任务充足，从高优先级到低优先级依次取任务 直到取满为止
    	logger.debug(":::to deque task size: "+popTaskCount);
        int taskCount = popTaskCount;
        for(int i=0; taskCount>0 && i<queues.size(); i++){
        	TaskQueue<T> tQueue = queues.get(i);
            int queueSize = tQueue.getSize();
            if(queueSize < 1){
            	//队列空，继续从其他队列读取
            	logger.debug(":::Queue "+tQueue.getQname()+" size "+queueSize);
                continue;
            }else if(queueSize >= taskCount){
            	//本批任务从一个队列中读取
            	logger.debug(":::Queue "+tQueue.getQname()+" size "+queueSize);
            	logger.debug(":::deque "+popTaskCount+" from "+tQueue.getQname());
            	tQueue.deque(taskBuffer, popTaskCount);
            	taskCount = 0;
            }else{
            	//本批任务从多个队列中读取
            	logger.debug(":::Queue "+tQueue.getQname()+" size "+queueSize);
            	logger.debug(":::deque "+queueSize+" from "+tQueue.getQname());
            	tQueue.deque(taskBuffer, queueSize);
            	taskCount = taskCount - queueSize;
            }
        }
    }
    /**
     * 初始化工作队列
     * @param size
     * @return
     */
    private Map<Integer,TaskQueue<T>> initTaskQueueArray(){
    	Map<Integer,TaskQueue<T>> tq = 
    		new HashMap<Integer,TaskQueue<T>>(DEFAULT_QUEUESIZE);
        tq.put(HIGH_PRIORITY, new TaskQueue<T>("HIGH"));
        tq.put(MED_PRIORITY, new TaskQueue<T>("MEDIUM"));
        tq.put(LOW_PRIORITY, new TaskQueue<T>("LOW"));
        return tq;
    }
    /**
     * 初始化工作线程，初始化时workers数组长度为最大线程池数量，
     * 但只先实例化 coreThreadSize 核心线程数大小
     * @param threadPoolName
     * @param taskBufferFactory
     * @param executor
     * @param workerBulkSize
     * @return
     */
    private List<Worker<T>> initWorkerArray(String threadPoolName, 
            TaskBufferFactory taskBufferFactory, Executor<T> executor){
    	List<Worker<T>> tw = new ArrayList<Worker<T>>(workThreadSize);
        for(int i=0; i< workThreadSize; i++){
            TaskBuffer<T> lTaskBuffer = taskBufferFactory.createInstance();
            Worker<T> worker = new Worker<T>((new StringBuilder())
                .append(threadPoolName).append("：Thread-").append(i).toString(),
                this, workerBulkSize, lTaskBuffer, executor);
            tw.add(worker);
        }
        return tw;
    }
}
