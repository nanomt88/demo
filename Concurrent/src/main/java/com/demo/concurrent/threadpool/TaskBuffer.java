package com.demo.concurrent.threadpool;

import java.util.List;

/**
 * @Copyright (c) Lictecs
 * @author xudong.hong@lictecs.com
 * @Date 2012-11-3
 * @Description: 任务缓存接口，任务执行过程中，
 *               当从队列中取出相应任务之后缓存起来，
 *               每个工作线程都对应一个缓冲区
 * @param <T>
 */

public interface TaskBuffer<T> {
	
    /**
     * 判断是否为空
     * @return
     */
    public boolean isBufferEmpty();
    
    /**
     * 获取任务的数量
     * @return
     */
    public int getTaskCount();
    
    /**
     * 向缓冲区添加任务
     * @param t
     */
    public void add(T t);
    
    /**
     * 向缓冲区添加任务
     * @param list
     */
    public void add(List<T> list);
    
    /**
     * 清空缓冲区
     */
    public void clear();
    
    /**
     * 执行缓冲区中的任务
     * @param excutor
     */
    public void execute(Executor<T> excutor);
}
