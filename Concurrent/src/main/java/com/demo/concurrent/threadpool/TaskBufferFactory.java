package com.demo.concurrent.threadpool;

/**
 * @Copyright 		(c) Lictecs
 * @author 			xudong.hong@lictecs.com
 * @Date 			2012-11-3
 * @Description: 
 *					TaskBuffer工厂类接口，生成TaskBuffer对象
 */
public interface TaskBufferFactory{
    /**
     * 创建TaskBuffer实例
     * @return
     */
    public abstract <T> TaskBuffer<T> createInstance();
}
