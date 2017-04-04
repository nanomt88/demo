package com.demo.future;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 ����9:30
 * @Description: //TODO
 */

public class FutureCall implements Callable<String> {

    private RealCall request = null;

    private boolean isDone = false;

    @Override
    public synchronized String call() throws InterruptedException {
        //���û��������������
        if(!isDone){
            wait();
        }
        //���֮���򷵻���ʵ�Ľ��
        return request.call();
    }

    /**
     * ����ִ�����֮����Ҫ���������
     * @param result
     */
    public synchronized void setResult(RealCall result) {
        if(isDone){
            return;
        }
        this.request = result;
        isDone = true;
        //�õ����֮�󣬻��������ٵȴ����߳�
        notifyAll();
    }

    public boolean isComplete(){
        return  isDone;
    }
}
