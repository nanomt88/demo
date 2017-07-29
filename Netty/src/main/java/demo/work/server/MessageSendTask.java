package demo.work.server;

import com.alibaba.fastjson.JSON;
import demo.work.ChargeQueryResponse;

/**
 *  发送查询结果到代收付系统
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-29 14:09
 **/
public class MessageSendTask implements Runnable{


    private ChargeQueryResponse message;

    public MessageSendTask(ChargeQueryResponse message){
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println("接受到查询报文："+message);
        // 发送 HTTP请求给 nova
        String jsonData = JSON.toJSONString(message);
        System.out.println("回调代收付系统开始,sid : "+message.getSid()+" ,data: " + jsonData);
        try {
            //TODO http请求发送结果

            System.out.println("回调代收付系统结束,sid : "+message.getSid()+" ,data: " + jsonData);
        } catch (Exception e) {
            System.out.println("[CALL_BACK_NOVA_ERROR] 回调代收付系统异常");
            e.printStackTrace();
        }
    }
}
