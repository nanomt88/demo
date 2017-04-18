package com.nanomt88.demo.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/15 上午10:54
 * @Description: //TODO
 */

public class SocketServerDemo {

    final static int PORT = 9876;

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT);
            System.out.println(" server start .. ");

            Socket socket = server.accept();
            new Thread(new ServerHandler(socket)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(server!=null){
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                server = null;
            }
        }

    }
}
