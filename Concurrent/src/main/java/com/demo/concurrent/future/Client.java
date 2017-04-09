package com.demo.concurrent.future;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 上午9:11
 * @Description:   客户端，用来包装future模式的类
 */

public class Client {

    public Callable request(final TaskMsg msg){

        //直接返回一个空的包装类，这个类不提供任务实现，具体的业务逻辑在线程中开启
        final FutureCall futureRequest = new FutureCall();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建真实的任务类
                RealCall realRequest = new RealCall(msg);
                //设置返回的结果到结果中
                futureRequest.setResult(realRequest);
            }
        }).start();
        return futureRequest;
    }

}
