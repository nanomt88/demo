package com.demo.threadpool;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @Copyright 		(c) Lictecs
 * @author 			xudong.hong@lictecs.com
 * @Date 			2012-10-30
 * @Description: 
 *					任务队列，遵循任务优先级排序 和 FIFO规则
 * @param <T>
 */
public class TaskQueue<T> {

	private List<T> taskQueue = null;
	private String qname = null;
	private Logger logger = Logger.getLogger(TaskQueue.class);

	public TaskQueue(String qname) {
		this.qname = qname;
		taskQueue = new ArrayList<T>();
	}
	
	public String getQname() {
		return qname;
	}
	
	/**
	 * 获取队列任务数量
	 * @return
	 */
	public int getSize() {
		return taskQueue.size();
	}
	
	/**
	 * 判断队列是否为空
	 * @return
	 */
	public boolean isEmpty() {
		return taskQueue.isEmpty();
	}
	
	/**
	 * 添加任务
	 * 队列中，优先级越高则任务的索引值越小，
	 * 相同优先级任务则根据先来先出FIFO原则
	 * @param task
	 */
	public void enque(T task) {
		if (task == null) {
			throw new IllegalArgumentException("task is null.");
		}
		synchronized (taskQueue) {
		    //队列优先级,默认为AbstractCallable.NORM_PRIORITY(5)
		    int lTaskPriority = AbstractCallable.NORM_PRIORITY;
		    if(task instanceof AbstractCallable){
		        lTaskPriority = ((AbstractCallable) task).getPriority();
		    }
			int lTaskCount = taskQueue.size();
			logger.debug("taskQueue " + qname +" size: "+lTaskCount);
			// 最大索引位置： 队列长度 减 1
			int lInsertionIndex = lTaskCount - 1;
			do {
				if (lInsertionIndex < 0) {
					//队列为空队列的情况，直接追加
					break;
				}
				//读取最低优先级的任务
				T oldTask =  taskQueue.get(lInsertionIndex);
				int oldTaskPrio = AbstractCallable.NORM_PRIORITY;
				if(oldTask instanceof AbstractCallable ){
				    oldTaskPrio = ((AbstractCallable) oldTask).getPriority();
				}
				//如果当前任务的优先级低或等于，则追加在此任务之后
				if(oldTaskPrio >= lTaskPriority){
					break;
				}
				//当前任务的优先级高，索引位置递减 1
				lInsertionIndex--;
			} while (true);
			// 插入任务到队列
			if (lInsertionIndex == lTaskCount - 1) {
				logger.debug("enque task to "+qname+" index "+(lTaskCount));
				taskQueue.add(task);
			} else {
				logger.debug("enque task to "+qname+" index "+(lInsertionIndex+1));
				taskQueue.add(lInsertionIndex+1, task);
			}
		}
	}
	
	/**
	 * 添加一组任务
	 * @param taskList
	 */
	public void enque(List<T> taskList) {
		if (taskList == null) {
			throw new IllegalArgumentException("aInTasks is null.");
		}
		int lTaskCount = taskList.size();
		for (int i = 0; i < lTaskCount; i++) {
			T lTask =  taskList.get(i);
			enque(lTask);
		}
	}
	
	/**
	 * 获取任务
	 * @param taskBuffer
	 * @param popTaskCount
	 * @throws Exception
	 */
	public void deque(TaskBuffer<T> taskBuffer,
			int popTaskCount) throws Exception {
		if (taskBuffer == null) {
			throw new IllegalArgumentException("taskBuffer is null.");
		}
		if (popTaskCount < 0) {
			throw new IllegalArgumentException("popTaskCount is invalid.");
		}
		if (taskQueue.isEmpty()) {
		    throw new IllegalStateException("taskQueue "+qname+" is empty.");
		}
		synchronized (taskQueue) {
			for (int lTaskCount = 0; !taskQueue.isEmpty()
					&& lTaskCount < popTaskCount; lTaskCount++) {
				T lTask =  taskQueue.remove(0);
				taskBuffer.add(lTask);
			}
		}
	}
}
