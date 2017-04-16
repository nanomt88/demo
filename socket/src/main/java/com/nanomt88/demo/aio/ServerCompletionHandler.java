package com.nanomt88.demo.aio;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/16 上午7:39
 * @Description: //TODO
 */

public class ServerCompletionHandler implements CompletionHandler<AsynchronousSocketChannel,Server> {
    /**
     * Invoked when an operation has completed.
     *
     * @param client     The result of the I/O operation.
     * @param attachment
     */
    @Override
    public void completed(AsynchronousSocketChannel client, Server attachment) {
        //当前客户端处理完成之后，需要处理后面的客户端连接
        //当有下一个客户端接入的时候，直接调用server的accept方法，这样一直反复执行
        //保证多个客户端都可以连接进来

        try {
            System.out.println("远程地址：" + client.getRemoteAddress());

            //tcp各项参数
            client.setOption(StandardSocketOptions.TCP_NODELAY, true);
            client.setOption(StandardSocketOptions.SO_SNDBUF, 1024);
            client.setOption(StandardSocketOptions.SO_RCVBUF, 1024);

            if (client.isOpen()) {
                System.out.println("client.isOpen：" + client.getRemoteAddress());
                read(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            attachment.serverSocketChannel.accept(attachment,this);
        }
    }

    private void read(AsynchronousSocketChannel socketChannel) {
        //读取数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socketChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer resultSize, ByteBuffer attachment) {
                //读取完成之后 重置标识
                attachment.flip();
                //
                System.out.println("Client say size : "+resultSize);
                //
                String msg = new String(attachment.array()).trim();
                System.out.println("Client say : " + msg);
                String response = "服务端收到消息了：" + msg+"\n";
                write(socketChannel,response);

            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });

    }

    private void write(AsynchronousSocketChannel socketChannel, String response) {
        //回写数据
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(response.getBytes());
            buffer.flip();
            socketChannel.write(buffer).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    /**
     * Invoked when an operation fails.
     *
     * @param exc        The exception to indicate why the I/O operation failed
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, Server attachment) {
        exc.printStackTrace();
    }
}
