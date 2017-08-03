package com.nanomt88.demo.drpc;

import org.apache.storm.thrift.TException;
import org.apache.storm.thrift.transport.TTransportException;
import org.apache.storm.utils.DRPCClient;

/**
 * 本地调用远程 DRPC 服务
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-03 8:10
 **/
public class DrpcClientDemo {

    public static void main(String[] args) throws TException {
        //远程 DRPC 端口默认 3772
        DRPCClient client = new DRPCClient(null,"192.168.1.140", 3772);
        for(String word : new String[]{"aaa", "bbb", "cccc"}){
            System.out.println(client.execute("exclamation", word));
        }
    }
}
