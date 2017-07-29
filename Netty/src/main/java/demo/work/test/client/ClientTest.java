package demo.work.test.client;

import demo.work.ChargeQueryRequest;
import demo.work.client.BeijingPayClient;

import java.util.Random;
import java.util.UUID;

/**
 * @author nanomt88@gmail.com
 * @create 2017-07-29 14:42
 **/
public class ClientTest {

    public static void main(String[] args) throws InterruptedException {

        BeijingPayClient client = new BeijingPayClient("127.0.0.1", 8080);
        while (true) {

            for (int i = 0; i < 3; i++) {
                ChargeQueryRequest request = new ChargeQueryRequest();
                request.setSid(UUID.randomUUID().toString());//requestNo
                request.setPan("6210000215748456289");//bankCardNo
                request.setSrcsid(UUID.randomUUID().toString().replace("-", ""));//chargeRequestNo
                request.setIpaddr("192.168.1.1");//ip
                request.setAmount(new Random().nextInt(100000) + "");//amount

                try {
                    client.sendMessage(request);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Thread.sleep(3000);
        }
    }
}
