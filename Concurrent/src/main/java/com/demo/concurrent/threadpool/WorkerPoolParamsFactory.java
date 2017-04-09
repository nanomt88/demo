package com.demo.concurrent.threadpool;


/**
 * @Copyright 		(c) Lictecs
 * @author 			xudong.hong@lictecs.com
 * @Date 			2013-1-6
 * @Description: 
 *					获取线程池配置参数工厂类
 */
public class WorkerPoolParamsFactory{
    
    private static int DEFAULT_WORKTHREADSIZE = 3;
    private static int DEFAULT_WORKERBULKSIZE = 1;
    private static int DEFAULT_FAILEDRETRIES = 1;
    
    /**
     * 获取线程池工作线程池数量
     * @return
     */
    public static int getWorkThreadSize(){
        String workThreadSize = "1";
        if(workThreadSize==null || workThreadSize.length()<1){
            System.out.println("workThreadSize invalid, use properties default val.");
            return DEFAULT_WORKTHREADSIZE;
        }
        try{
            Integer val = Integer.parseInt(workThreadSize);
            if(val > 0 ){
                return val;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("workThreadSize invalid, use properties default val.");
        return DEFAULT_WORKTHREADSIZE;
    }
    
    /**
     * 获取线程池每次执行任务数
     * @return
     */
    public static int getWorkerBulkSize(){
        String workerBulkSize = "1";
        if(workerBulkSize==null || workerBulkSize.length()<1){
            System.out.println("workerBulkSize invalid, use properties default val.");
            return DEFAULT_WORKERBULKSIZE;
        }
        try{
            Integer val = Integer.parseInt(workerBulkSize);
            if(val > 0 ){
                return val;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("workerBulkSize invalid, use properties default val.");
        return DEFAULT_WORKERBULKSIZE;
    }
    
    /**
     * 获取线程池执行失败尝试次数
     * @return
     */
    public static int getThreadFailedRetries(){
        String failedRetries = "1";
        if(failedRetries==null || failedRetries.length()<1){
            System.out.println("failedRetries invalid, use properties default val.");
            return DEFAULT_FAILEDRETRIES;
        }
        try{
            Integer val = Integer.parseInt(failedRetries);
            if(val > 0 ){
                return val;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("failedRetries invalid, use properties default val.");
        return DEFAULT_FAILEDRETRIES;
    }
}
