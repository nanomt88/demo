package com.demo.concurrent.threadpool;

/**
 * @Copyright 		(c) Lictecs
 * @author 			xudong.hong@lictecs.com
 * @Date 			2012-11-2
 * @Description: 
 *					添加对任务执行失败、异常的处理
 */

public interface CallableListener {
    /**
     * 异常监听处理
     * @param call
     * @param ex
     */
    void errorHandle(Callable call, Exception ex);
    
    /**
     * 失败监听处理
     * @param call
     */
    void failedHandle(Callable call);
}
