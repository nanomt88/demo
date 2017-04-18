package com.nanomt88.demo.nio;

import com.nanomt88.demo.bio.ServerHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/15 下午4:13
 * @Description: //TODO
 */

public class NIOServer implements Runnable{

    //1. 多路复用器（管理所有的通道）
    private Selector selector = null;
    //2. 建立读写缓冲区
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    public NIOServer(int port){
        try {
            //1. 打开多路选择复用器
            this.selector = Selector.open();
            //2. 打开服务器通道
            ServerSocketChannel channel = ServerSocketChannel.open();
            //3. 设置服务器通道为非堵塞模式
            channel.configureBlocking(false);
            //4. 绑定地址
            channel.bind(new InetSocketAddress(port));
            //5. 把服务器通道注册到多路复用器上，并且监听堵塞事件
            channel.register(this.selector, SelectionKey.OP_ACCEPT);

            System.out.println("server started in port :" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true){

            try {
                //1. 必须要让多路复用器开始监听
                this.selector.select();

                //2. 返回多路复用器已经选择的结果集
                Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
                //3. 迭代遍历
                while (iterator.hasNext()){
                    //4. 获取一个选择的元素
                    SelectionKey selectionKey = iterator.next();

                    //5. 如果是有效的则进行处理
                    if(selectionKey.isValid()){
                        //6. 如果为堵塞状态，则连接
                        if(selectionKey.isAcceptable()){
                            this.accept(selectionKey);
                        }
                        //7. 如果为可读状态，进行读取数据
                        if(selectionKey.isReadable()){
                            this.read(selectionKey);
                        }
                        //8. 如果为可写状态，写数据
                        if(selectionKey.isWritable()){
                            this.write(selectionKey);
                        }
                    }
                    //9. 直接从选择器中删除该元素
                    iterator.remove();

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void write(SelectionKey selectionKey) {
        try {
            this.writeBuffer.clear();

            SocketChannel ssc =  (SocketChannel) selectionKey.channel();
            System.out.println("给客户端发送消息：收到你的消息啦");
            writeBuffer.put(new String("收到你的消息啦").getBytes());
            writeBuffer.flip();

            ssc.write(writeBuffer);

            ssc.register(this.selector, SelectionKey.OP_WRITE);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void read(SelectionKey selectionKey) {
        try {

            //1. 清空缓冲区中旧数据
            this.readBuffer.clear();
            //2. 获取之前注册的socket通道对象
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            //3. 读取数据
            int count = channel.read(this.readBuffer);
            //4. 没有数据则返回
            if(count == -1){
                selectionKey.channel().close();
                selectionKey.cancel();
                return;
            }
            //5. 有数据进行读取，读取之前先要将ByteBuffer进行复位（把position、limit进行复位）
            this.readBuffer.flip();
            //6. 根据缓冲区中的数据长度创建相应的数组
            byte[] bytes = new byte[this.readBuffer.remaining()];
            //7. 接收缓冲区数据
            this.readBuffer.get(bytes);
            //8. 打印结果
            String msg = new String(bytes);
            System.out.println("Client say : " + msg);

            //9. 给客户端回写数据

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void accept(SelectionKey selectionKey) {
        try {
            //1. 获取服务通道
            ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
            //2. 执行堵塞方法
            SocketChannel socketChannel = channel.accept();
            //3. 设置堵塞模式：非堵塞
            socketChannel.configureBlocking(false);
            //4. 注册到多路复用器上，并且设置读取标识
            socketChannel.register(this.selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new NIOServer(9876)).start();
    }
}
