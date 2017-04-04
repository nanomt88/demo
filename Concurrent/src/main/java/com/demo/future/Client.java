package com.demo.future;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 ����9:11
 * @Description:   �ͻ��ˣ�������װfutureģʽ����
 */

public class Client {

    public Callable request(final TaskMsg msg){

        //ֱ�ӷ���һ���յİ�װ�࣬����಻�ṩ����ʵ�֣������ҵ���߼����߳��п���
        final FutureCall futureRequest = new FutureCall();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //������ʵ��������
                RealCall realRequest = new RealCall(msg);
                //���÷��صĽ���������
                futureRequest.setResult(realRequest);
            }
        }).start();
        return futureRequest;
    }

}
