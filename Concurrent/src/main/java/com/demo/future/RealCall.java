package com.demo.future;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 ����10:37
 * @Description: //TODO
 */

public class RealCall implements Callable<String> {

    private String result ;

    public RealCall(TaskMsg task){
        try {
            //ģ��ִ������ҵ������ ���ĵ�ʱ��
            Thread.currentThread().sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //���ؽ��
        result = task.getId()+ " : " +task.getName() + " : " +task.getPrice();
    }

    public String call() {
        return result;
    }

}
