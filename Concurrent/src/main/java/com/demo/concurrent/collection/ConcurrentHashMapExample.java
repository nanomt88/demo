package com.demo.concurrent.collection;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 上午8:47
 * @Description: //TODO
 */

public class ConcurrentHashMapExample {

    public static void main(String[] args) {

        ConcurrentHashMap<Integer,String> map = new ConcurrentHashMap<Integer,String>();
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        String val = map.putIfAbsent(4,"4");
        System.out.println(val);
        for (Enumeration<Integer> keys = map.keys(); keys.hasMoreElements();) {
            Integer key = keys.nextElement();
            System.out.println("KEY:"+ key + ", VALUE:" + map.get(key));
        }
    }
}
