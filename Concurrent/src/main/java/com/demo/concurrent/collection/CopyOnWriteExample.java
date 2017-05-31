package com.demo.concurrent.collection;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/6 下午8:04
 * @Description: //TODO
 */

public class CopyOnWriteExample {
    public static void main(String[] args) {

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        boolean flag = list.addIfAbsent("ccc");
        System.out.println(flag);
        flag = list.addIfAbsent("ddd");
        System.out.println(flag);

        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
            System.out.println(iterator.next());
        }
    }
}
