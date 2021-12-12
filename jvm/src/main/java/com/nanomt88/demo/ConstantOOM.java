package com.nanomt88.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/5/6 上午10:10
 * @Description: //TODO
 */

public class ConstantOOM {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        //-Xmx10m -Xms10m -XX:+PrintGCDetails
        List<String> list = new ArrayList<String>();
        int i = 0;
        while (true) {
            list.add(String.valueOf(i++).intern());
        }
    }


}
