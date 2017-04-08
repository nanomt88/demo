package com.demo.concurrent.threadpool;

/**
 * @Copyright 		(c) Lictecs
 * @author 			xudong.hong@lictecs.com
 * @Date 			2012-10-30
 * @Description: 
 *					任务队列执行调度类
 * @param <T>
 */
public interface Executor<T>{
    /**
     * 执行任务队列中的任务，成功返回0，失败返回-1;
     * @param t
     * @return  成功返回 0，失败返回-1;
     */
    int execute(T t);
}
