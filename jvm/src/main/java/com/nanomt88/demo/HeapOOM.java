package com.nanomt88.demo;

import java.util.Vector;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/5/6 下午9:21
 * @Description:    堆内存溢出实验
 *
 */

public class HeapOOM {

    public static void main(String[] args) {

        //-Xms3m -Xmx3m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/Users/tcdd/Desktop/oom1.dump
        //堆内存溢出
        Vector v = new Vector();
        for (int i = 0; i < 5 ; i++) {
            //申请1M内存
            v.add(new byte[1 * 1024 * 1024]);
        }
    }
}
