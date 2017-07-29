package demo.work.test.server;

import demo.work.server.BeijingPayServer;

public class BeijingPayServerTest {

    public static void main(String[] args) throws InterruptedException {

        BeijingPayServer server = new BeijingPayServer(8181);
        System.out.println("服务端启动");
        Thread.sleep(Integer.MAX_VALUE);
    }

}