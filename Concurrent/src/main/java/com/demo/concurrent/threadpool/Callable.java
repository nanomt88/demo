package com.demo.concurrent.threadpool;

/**
 * 
 * @Copyright 		(c) Lictecs
 * @author 			xudong.hong@lictecs.com
 * @Date 			2012-10-29
 * @Description: 
 *					业务接口，子类AbstractCallable扩展了设置任务优先级、监听器的功能
 */
public interface Callable{
    
	public static final int RESULT_OK = 0;
	public static final int RESULT_FAILED = -1;
	
    /**
     * 执行业务，可能抛出异常的任务，根据异常处理流程对异常进行处理，
     * 如果是AbstractCallable对象，先根据注册的异常监听器CallableListener处理，
     * 然后再根据ExecutorListener的异常处理
     * @return  返回值, 成功为 RESULT_OK，失败为  RESULT_FAILED 
     * @throws Exception  出错则抛出异常
     */
    int call() throws Exception;
}