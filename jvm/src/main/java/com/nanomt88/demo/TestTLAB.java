package com.nanomt88.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/5/8 上午7:29
 * @Description: //TODO
 */

public class TestTLAB {

    public static void main(String[] args) {

        //参数：-Xmx30M -Xms30M -XX:+UseSerialGC -XX:+PrintGCDetails -XX:PretenureSizeThreshold=1024000
        /**
         *  使用 PretenureSizeThreshold 参数，单个对象超过1024000，所以直接分配到老年代中
         Heap
         def new generation   total 9216K, used 2225K [0x00000007be200000, 0x00000007bec00000, 0x00000007bec00000)
            eden space 8192K,  27% used [0x00000007be200000, 0x00000007be42c478, 0x00000007bea00000)
            from space 1024K,   0% used [0x00000007bea00000, 0x00000007bea00000, 0x00000007beb00000)
            to   space 1024K,   0% used [0x00000007beb00000, 0x00000007beb00000, 0x00000007bec00000)
         tenured generation   total 20480K, used 5120K [0x00000007bec00000, 0x00000007c0000000, 0x00000007c0000000)
            the space 20480K,  25% used [0x00000007bec00000, 0x00000007bf100050, 0x00000007bf100200, 0x00000007c0000000)
         Metaspace       used 3290K, capacity 4494K, committed 4864K, reserved 1056768K
            class space    used 367K, capacity 386K, committed 512K, reserved 1048576K
         *
         */
//        Map<Integer, byte[]> map1 = new HashMap<>();
//        for(int i=0; i< 5; i++){
//            byte[] b = new byte[1024*1024];
//            map1.put(i, b);
//        }


        //这种现象原因为：虚拟机对于体积不大的对象 会优先把数据分配到TLAB区域中，因此就失去了在老年代分配的机会
        //参数：-Xmx30M -Xms30M -XX:+UseSerialGC -XX:+PrintGCDetails -XX:PretenureSizeThreshold=1000 （-XX:-UseTLAB）
        /**
         *  虽然使用了PretenureSizeThreshold参数，但是大于1K的数据并没有进入老年代，而是在新生代中
         *      原因： JVM默认开启了TLAB空间，对于不大的对象会直接分配到TLAB空间中，用来加速线程分配对象的效率
         Heap
         def new generation   total 9216K, used 7589K [0x00000007be200000, 0x00000007bec00000, 0x00000007bec00000)
            eden space 8192K,  92% used [0x00000007be200000, 0x00000007be969750, 0x00000007bea00000)
            from space 1024K,   0% used [0x00000007bea00000, 0x00000007bea00000, 0x00000007beb00000)
             to   space 1024K,   0% used [0x00000007beb00000, 0x00000007beb00000, 0x00000007bec00000)
         tenured generation   total 20480K, used 75K [0x00000007bec00000, 0x00000007c0000000, 0x00000007c0000000)
            the space 20480K,   0% used [0x00000007bec00000, 0x00000007bec12d90, 0x00000007bec12e00, 0x00000007c0000000)
         Metaspace       used 3312K, capacity 4494K, committed 4864K, reserved 1056768K
            class space    used 369K, capacity 386K, committed 512K, reserved 1048576K
         */

        /**
         *  在上面的参数基础上加入 ： -XX:-UseTLAB ，禁用 TLAB区域后，对象分配就会进入老年代
         Heap
         def new generation   total 9216K, used 1007K [0x00000007be200000, 0x00000007bec00000, 0x00000007bec00000)
             eden space 8192K,  12% used [0x00000007be200000, 0x00000007be2fbc88, 0x00000007bea00000)
             from space 1024K,   0% used [0x00000007bea00000, 0x00000007bea00000, 0x00000007beb00000)
             to   space 1024K,   0% used [0x00000007beb00000, 0x00000007beb00000, 0x00000007bec00000)
         tenured generation   total 20480K, used 6163K [0x00000007bec00000, 0x00000007c0000000, 0x00000007c0000000)
            the space 20480K,  30% used [0x00000007bec00000, 0x00000007bf204d98, 0x00000007bf204e00, 0x00000007c0000000)
         Metaspace       used 3309K, capacity 4494K, committed 4864K, reserved 1056768K
            class space    used 369K, capacity 386K, committed 512K, reserved 1048576K
         */
        Map<Integer, byte[]> m = new HashMap<Integer, byte[]>();
        for(int i=0; i< 5*1024; i++){
            byte[] b = new byte[1024];
            m.put(i, b);
        }

    }
}
