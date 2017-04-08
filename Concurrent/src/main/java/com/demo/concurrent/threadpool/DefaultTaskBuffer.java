package com.demo.concurrent.threadpool;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class DefaultTaskBuffer<T> implements TaskBuffer<T> {

	private int taskCount = 0;
	private Object lock = new Object();
	private List<T> taskCache = new ArrayList<T>();
	private Logger logger = Logger.getLogger(DefaultTaskBuffer.class);

	@Override
	public boolean isBufferEmpty() {
		return taskCount == 0;
	}

	@Override
	public int getTaskCount() {
		return taskCount;
	}
	
	/**
	 * 增加单个任务
	 */
	@Override
	public void add(T task) {
		if (task == null) {
			throw new IllegalArgumentException("task is null");
		}
		synchronized (lock) {
			taskCache.add(task);
			taskCount++;
		}
	}
	
	/**
	 * 批量增加要执行的任务
	 */
	@Override
	public void add(List<T> taskList) {
		if (taskList == null) {
			throw new IllegalArgumentException("taskList is null");
		}
		int tnums = taskList.size();
		//检查将要增加到执行缓冲中的任务，仅增加和进一步处理不为null的任务
		for (int i = 0; i < tnums; i++) {
			T lTask = (T) taskList.get(i);
			if(lTask == null){
				logger.warn((new StringBuilder())
					.append("WARN: task at index=").append(i)
					.append(" is null.").toString());
			}else{
				add(lTask);
			}
		}
	}
	
	/**
	 * 执行缓冲区中的任务
	 */
	@Override
	public void execute(Executor<T> executor) {
		if (executor == null) {
			throw new IllegalArgumentException("aInTaskExecutor is NULL");
		}
		synchronized (lock) {
			int lTaskCount = taskCache.size();
			for (int i = 0; i < lTaskCount; i++) {
				T lTask = taskCache.get(i);
				executor.execute(lTask);
			}
		}
	}
	
	/**
	 * 清除缓存，释放空间
	 */
	@Override
	public void clear() {
		synchronized (lock) {
			if (taskCache != null) {
				taskCache.clear();
			}
			taskCount = 0;
		}
	}
}
