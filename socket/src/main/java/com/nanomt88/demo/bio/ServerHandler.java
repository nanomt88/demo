package com.nanomt88.demo.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/15 上午11:19
 * @Description: //TODO
 */

public class ServerHandler implements Runnable {


    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String body = null;
            while (true) {
                body = in.readLine();
                if (body == null) {
                    break;
                }
                System.out.println("Client say : "+body);
                out.println("服务器端收到信息："+body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out != null){
                out.close();
            }
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }

    }
}
