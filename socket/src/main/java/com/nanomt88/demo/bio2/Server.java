package com.nanomt88.demo.bio2;

import com.nanomt88.demo.bio.ServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/15 下午3:56
 * @Description: //TODO
 */

public class Server {

    final static int PORT = 9876;

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT);
            System.out.println(" server start .. ");

            ServerHandleThreadPool pool = new ServerHandleThreadPool(50,100);
            Socket socket = null;

            while (true){
                socket = server.accept();
                pool.execute(new ServerHandler(socket));
            }

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
