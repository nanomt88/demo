package com.demo.future;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 上午9:30
 * @Description: //TODO
 */

public class FutureCall implements Callable<String> {

    private RealCall request = null;

    private boolean isDone = false;

    @Override
    public synchronized String call() throws InterruptedException {
        //如果没有完成任务，则堵塞
        if(!isDone){
            wait();
        }
        //完成之后，则返回真实的结果
        return request.call();
    }

    /**
     * 任务执行完成之后，需要将结果返回
     * @param result
     */
    public synchronized void setResult(RealCall result) {
        if(isDone){
            return;
        }
        this.request = result;
        isDone = true;
        //拿到结果之后，唤醒所有再等待的线程
        notifyAll();
    }

    public boolean isComplete(){
        return  isDone;
    }
}
