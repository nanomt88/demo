package com.nanomt88.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/5/8 上午7:29
 * @Description: //TODO
 */

public class TestTLAB2 {

    public static void alloc(){
        byte[] b = new byte[2];
    }
    public static void main(String[] args) {

        //TLAB分配
        //参数：-XX:+UseTLAB -XX:+PrintTLAB -XX:+PrintGC -XX:TLABSize=102400 -XX:-ResizeTLAB -XX:TLABRefillWasteFraction=100 -XX:-DoEscapeAnalysis -server
        for(int i=0; i<10000000;i++){
            alloc();
        }


    }
}
