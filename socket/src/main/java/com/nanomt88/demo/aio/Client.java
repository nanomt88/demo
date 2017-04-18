package com.nanomt88.demo.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/16 上午8:00
 * @Description: //TODO
 */

public class Client implements Runnable{

    private AsynchronousSocketChannel socketChannel;

    public Client() throws IOException {
        this.socketChannel = AsynchronousSocketChannel.open();
    }

    public void connect(int port){
        socketChannel.connect(new InetSocketAddress("127.0.0.1",port));
    }

    @Override
    public void run() {
        while (true){

        }
    }

    public void read(){
        try {

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            socketChannel.read(buffer).get();

            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);

            System.out.println(new String(bytes,"UTF-8").trim());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void write(String request){
        socketChannel.write(ByteBuffer.wrap(request.getBytes()));
        read();
    }

    public static void main(String[] args) throws IOException {
        Client c1 = new Client();
        c1.connect(8888);
        new Thread(c1).start();

        c1.write(" hello, i am client 1...");
    }
}
