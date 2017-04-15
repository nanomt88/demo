package com.nanomt88.demo.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/15 下午5:34
 * @Description: //TODO
 */

public class NIOClient {

    public static void main(String[] args) {

        //创建远程连接的地址
        InetSocketAddress address = new InetSocketAddress(9876);
        //声明连接通道
        SocketChannel socketChannel = null;
        //建立缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try{

            //打开通道
            socketChannel = SocketChannel.open();
            //建立连接
            socketChannel.connect(address);

            while(true){
                //定义一个字节数组，然后使用系统输入数据线
                byte[] bytes = new byte[1024];
                System.in.read(bytes);

                //把数据放到缓存区，等待发送
                buffer.put(bytes);
                //把缓冲区的数据进行复位（每次读取前都要复位）
                buffer.flip();
                //写数据
                socketChannel.write(buffer);
                //清空缓冲区
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socketChannel != null){
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
