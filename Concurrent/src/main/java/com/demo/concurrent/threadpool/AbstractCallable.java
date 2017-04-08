package com.demo.concurrent.threadpool;

/**
 * @Copyright 		(c) Lictecs
 * @author 			xudong.hong@lictecs.com
 * @Date 			2012-11-2
 * @Description: 
 *					实现任务接口，扩展Callable接口
 */

public abstract class AbstractCallable implements Callable{
    /** 任务优先级的最小值   */
    public final static int MIN_PRIORITY = 1;
    /** 任务优先级的默认值   */
    public final static int NORM_PRIORITY = 5;
    /** 任务优先级的最大值   */
    public final static int MAX_PRIORITY = 10;
    
    private int priority = NORM_PRIORITY;
    private CallableListener errorLisenter = null;
    
    /**
     * 缺省构造函数
     */
    public AbstractCallable(){
    	this.priority = NORM_PRIORITY;
    }
    
    /**
     * @param priority  优先级, 默认为 NORM_PRIORITY(5)
     */
    public AbstractCallable(int priority){
        if(priority < MIN_PRIORITY || priority> MAX_PRIORITY)
            throw new IllegalArgumentException("priority");
        this.priority = priority;
    }
    
    /**
     * 设置任务的优先级,默认为 NORM_PRIORITY(5)
     * @param priority
     */
    public final void setPriority(int priority){
        if(priority < MIN_PRIORITY || priority> MAX_PRIORITY)
            throw new IllegalArgumentException("priority");
        this.priority = priority;
    }
    
    /**
     * 获取任务的优先级,默认为 NORM_PRIORITY(5)
     * @param priority
     */
    public final int getPriority(){
        return priority;
    }
    
    /**
     * 注册错误监听
     * @param errorLisenter
     */
    public void registListener(CallableListener lisenter){
        if(lisenter != null)
            this.errorLisenter = lisenter;
    }
    
    /**
     * 当调用call事 异常处理
     * @param ex
     */
    protected void errorHandle(Exception ex) {
        if(errorLisenter != null)
            errorLisenter.errorHandle(this, ex);
    }

    @Override
    public abstract int call() throws Exception;
}
