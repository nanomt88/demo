package com.demo.collection;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/5 ����6:19
 * @Description: //TODO
 */

public class ConcurrentSkipListMapExample {

    public static void main(String[] args) {

        ConcurrentSkipListMap<String,String> map = new ConcurrentSkipListMap<String,String>();
        map.put("3","333");
        map.put("2","222");
        map.put("1","111");
        map.put("5","5555");
        //ԭʼ˳��
        for(Iterator<String> iterator = map.descendingKeySet().iterator(); iterator.hasNext();){
            String key = iterator.next();

            System.out.println(" KEY ["+key+"]  VALUE ["+ map.get(key) +"]");

        }
        //����֮���˳��
        for(Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();){
            String key = iterator.next();

            System.out.println(" KEY ["+key+"]  VALUE ["+ map.get(key) +"]");

        }

        System.out.println("first key : " + map.firstEntry().getKey());
    }

}
