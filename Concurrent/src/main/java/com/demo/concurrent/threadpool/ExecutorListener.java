package com.demo.concurrent.threadpool;

/**
 * 
 * @Copyright 		(c) Lictecs
 * @author 			xudong.hong@lictecs.com
 * @Date 			2012-10-29
 * @Description: 
 *					在Executor执行过程中，管理Executor的执行过程
 */
public interface ExecutorListener {
	
    /**
     * 在Executor.execute方法执行之前处理
     */
	public void beforeExecute();
	
	/**
	 * 在Executor.execute方法执行之后处理
	 */
	public void afterExecute();
	
	/**
	 * 在Executor.execute方法失败之后处理
	 * execute返回0为成功，返回-1为失败
	 */
	public void faildExecute();
	
	/**
	 * 在Executor.execute方法执行异常时处理
	 * @param ex 异常
	 */
	public void exceptionHandle(Exception ex);
	
	/**
     * 在Executor.execute方法执行异常需要重试时处理
     * @param i 重试次数
     */
	public void retryExecute(int i) throws Exception;
}