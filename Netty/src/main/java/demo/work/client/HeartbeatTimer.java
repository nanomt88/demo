package demo.work.client;

import java.util.TimerTask;

/**
 * 发送心跳 timer
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-29 17:46
 **/
public class HeartbeatTimer extends TimerTask {

    BeijingPayClient socket ;

    public HeartbeatTimer( BeijingPayClient socket){
        this.socket = socket;
    }

    /**
     * 此线程定时给北京支付发送心跳报文
     */
    public void run() {
        try {
            socket.sendMessage("0000");
        } catch (Exception e) {
            System.out.println("HEARTBEAT_ERROR:time:" + System.currentTimeMillis());
            e.printStackTrace();
        }

    }
}
