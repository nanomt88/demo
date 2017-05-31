package com.demo.concurrent.collection;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/6 下午9:33
 * @Description: //TODO
 */

public class LinkedBlockingQueueExample {
    public static void main(String[] args) {
        //如果不设置队列长度，默认为Integer.MAX_VAL
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue();
        queue.offer("bbb");
        queue.offer("aaa");
        queue.offer("ccc");
        boolean flag = queue.offer("aaa");
        System.out.println("插入是否成功："+flag);
        for (Iterator iterator= queue.iterator(); iterator.hasNext(); ){
            System.out.println(iterator.next());
        }
    }
}
