package com.demo.concurrent.collection;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/5 下午6:19
 * @Description: //TODO
 */

public class ConcurrentSkipListMapExample {

    public static void main(String[] args) {

        ConcurrentSkipListMap<String,String> map = new ConcurrentSkipListMap<String,String>();
        map.put("3","333");
        map.put("2","222");
        map.put("1","111");
        map.put("5","5555");
        //原始顺序
        for(Iterator<String> iterator = map.descendingKeySet().iterator(); iterator.hasNext();){
            String key = iterator.next();

            System.out.println(" KEY ["+key+"]  VALUE ["+ map.get(key) +"]");

        }
        //排序之后的顺序
        for(Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();){
            String key = iterator.next();

            System.out.println(" KEY ["+key+"]  VALUE ["+ map.get(key) +"]");

        }

        System.out.println("first key : " + map.firstEntry().getKey());
    }

}
