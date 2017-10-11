package com.demo.concurrent.collection;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/7/27 下午8:39
 * @Description: //TODO
 */

public class LruCache<K, V> extends LinkedHashMap<K, V> {
    private int cacheSize;

    public LruCache(int cacheSize) {
        super(cacheSize, 0.75F, true);
        this.cacheSize = cacheSize;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() >= cacheSize;
    }

    public static void main(String[] args) {
        String flag="100-";
        LruCache<String,Integer> cache=new LruCache(1000);
        for (int i=0;i<10000;i++){
            cache.put(i+"-",i);
            if(i%100==0){
                System.out.print(cache.get(flag)+",");
            }
        }
        System.out.println();
        System.out.println(cache.get(flag));
        System.out.println(cache.get(flag));
        System.out.println(cache.get(flag));
//        client.put(1000+"",1002222);
//        System.out.println(client.get(flag));
//        System.out.println(client.get(flag));
        Iterator it=cache.keySet().iterator();
        while (it.hasNext()){
            System.out.print(it.next());
            System.out.print(",");
        }

    }
}
