package com.demo.concurrent.threadpool;


public class DefaultExecutor<T extends Callable> implements Executor<T>{
	
	private ExecutorListener execLsnr = null;
	private int singleRetries = 1;
	
	public DefaultExecutor(){
		singleRetries = WorkerPoolParamsFactory.getThreadFailedRetries();
	}
	
	/**
	 * @param executorListenner  Executor监听对象
	 */
	public DefaultExecutor(ExecutorListener executorListenner) {
		execLsnr = executorListenner;
	}

	@Override
	public int execute(T callable) {
	    int lRet = single(callable);
		return lRet;
	}
	
	/**
	 * 任务执行过程
	 * @param callable 要执行的任务
	 * @return 执行结果，成功返回 RESULT_OK， 否则返回 RESULT_FAILED
	 */
	private int single(T callable) {
		int lRetries = 0;
		do {
			try {
				//1 失败后重试执行，调用侦听器的失败处理
				if (lRetries != 0) {
					if(execLsnr!=null)
						execLsnr.retryExecute(lRetries);
				}
				//2 任务执行前处理
				if(execLsnr!=null)
					execLsnr.beforeExecute();
				//3 调用执行业务
				int result = callable.call();
				//4 如果执行失败，调用侦听器的失败处理，然后执行结束
				if(result == Callable.RESULT_FAILED){
					if(execLsnr!=null)
						execLsnr.faildExecute();
				    return result;
				}
		        //任务执行后处理
				if(execLsnr!=null)
					execLsnr.afterExecute();
				return Callable.RESULT_OK;
			} catch (Exception e) {
				e.printStackTrace();
				//判断超过重试次数 则调用异常处理
				if (++lRetries >= singleRetries) {
					//调用任务自身的错误处理
                    if(callable instanceof AbstractCallable)
                        ((AbstractCallable)callable).errorHandle(e);
                    //调用侦听器的错误处理
                    if(execLsnr!=null)
                    	execLsnr.exceptionHandle(e);
                    break; 
                }
			} catch (Throwable t) {
				t.printStackTrace();
				return Callable.RESULT_FAILED;
			}
		} while (true);
		return Callable.RESULT_FAILED;
	}
}
