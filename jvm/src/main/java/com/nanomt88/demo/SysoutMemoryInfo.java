package com.nanomt88.demo;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/5/6 下午6:54
 * @Description:
 *      打印系统内存信息
 *  	运行JVM参数：
 *  	    -Xms5m -Xmx20m -XX:+PrintGCDetails -XX:+UseSerialGC -XX:+PrintCommandLineFlags
 */

public class SysoutMemoryInfo {

    public static void main(String[] args) {

        //查看GC信息
        System.out.println("max memory:" + Runtime.getRuntime().maxMemory()/1024L + " K");
        System.out.println("free memory:" + Runtime.getRuntime().freeMemory() / 1024L + " K");
        System.out.println("total memory:" + Runtime.getRuntime().totalMemory() / 1024L + " K");

        byte[] b1 = new byte[1*1024*1024];
        System.out.println("分配了1M");
        System.out.println("max memory:" + Runtime.getRuntime().maxMemory() / 1024L + " K");
        System.out.println("free memory:" + Runtime.getRuntime().freeMemory() / 1024L + " K");
        System.out.println("total memory:" + Runtime.getRuntime().totalMemory() / 1024L + " K");

        byte[] b2 = new byte[4*1024*1024];
        System.out.println("分配了4M");
        System.out.println("max memory:" + Runtime.getRuntime().maxMemory() / 1024L + " K");
        System.out.println("free memory:" + Runtime.getRuntime().freeMemory() / 1024L + " K");
        System.out.println("total memory:" + Runtime.getRuntime().totalMemory() / 1024L + " K");

        /**
         *  -XX:+PrintGCDetails 参数打印：
         *
         Heap
         def new generation   total 1920K, used 67K [0x00000007bec00000, 0x00000007bee10000, 0x00000007bf2a0000)
            eden space 1728K,   3% used [0x00000007bec00000, 0x00000007bec10cc8, 0x00000007bedb0000)
            from space 192K,   0% used [0x00000007bedb0000, 0x00000007bedb0000, 0x00000007bede0000)
            to   space 192K,   0% used [0x00000007bede0000, 0x00000007bede0000, 0x00000007bee10000)
         tenured generation   total 8196K, used 5556K [0x00000007bf2a0000, 0x00000007bfaa1000, 0x00000007c0000000)
            the space 8196K,  67% used [0x00000007bf2a0000, 0x00000007bf80d298, 0x00000007bf80d400, 0x00000007bfaa1000)
         Metaspace       used 3305K, capacity 4494K, committed 4864K, reserved 1056768K
            class space    used 369K, capacity 386K, committed 512K, reserved 1048576K

         def new generation ： 新生代
         tenured generation ： 老年代
         Metaspace          ： 元数据区（方法区）

         */

    }
}
