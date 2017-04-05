package com.demo.future;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 上午10:37
 * @Description: //TODO
 */

public class RealCall implements Callable<String> {

    private String result ;

    public RealCall(TaskMsg task){
        try {
            //模拟执行真正业务流程 消耗的时间
            Thread.currentThread().sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //返回结果
        result = task.getId()+ " : " +task.getName() + " : " +task.getPrice();
    }

    public String call() {
        return result;
    }

}
