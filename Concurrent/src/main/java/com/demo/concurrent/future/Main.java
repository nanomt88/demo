package com.demo.concurrent.future;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 上午10:40
 * @Description: //TODO
 */

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();

        TaskMsg msg = new TaskMsg();
        msg.setId(1);
        msg.setName("name");
        msg.setPrice(100);

        Callable request = client.request(msg);
        Thread.currentThread().sleep(2000);
        System.out.println("线程完成其他任务...");
        System.out.println(request.call());
    }

}
