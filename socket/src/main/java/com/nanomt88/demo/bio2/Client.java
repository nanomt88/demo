package com.nanomt88.demo.bio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/15 上午11:07
 * @Description: //TODO
 */

public class Client {

    final static int PORT = 9876;
    final static String ADDRESS = "127.0.0.1";


    public static void main(String[] args) {

        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            socket = new Socket(ADDRESS, PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream() ,true);

            out.println("hello，我是xxx 客户端。。。");
            out.println("你收到我的信息了么 收到了么。。。");
            String response = in.readLine();
            System.out.println("Server say :"+response);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out != null){
                out.close();
            }
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket = null;
        }
    }
}
