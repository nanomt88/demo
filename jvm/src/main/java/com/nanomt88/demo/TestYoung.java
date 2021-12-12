package com.nanomt88.demo;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/5/6 下午8:30
 * @Description: //TODO
 */

public class TestYoung {

    public static void main(String[] args) {

        //第一次配置
        //-Xms20m -Xmx20m -Xmn1m -XX:SurvivorRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC
        //      SurvivorRatio=2：表示 Eden ：from（to） = 2 ：1 ；Eden 占用50%
        //          新生代：1M ； eden：512K ； from ：256K ；to ：256K
        //          老年代：19M
        /**
         *  GC 打印日志如下：
         [GC (Allocation Failure) [DefNew: 512K->255K(768K), 0.0009720 secs] 512K->267K(20224K), 0.0010034 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 767K->75K(768K), 0.0012062 secs] 779K->342K(20224K), 0.0012253 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 587K->121K(768K), 0.0022859 secs] 854K->388K(20224K), 0.0023190 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         Heap
         def new generation   total 768K, used 289K [0x00000007bec00000, 0x00000007bed00000, 0x00000007bed00000)
         eden space 512K,  32% used [0x00000007bec00000, 0x00000007bec29f68, 0x00000007bec80000)
         from space 256K,  47% used [0x00000007becc0000, 0x00000007becde540, 0x00000007bed00000)
         to   space 256K,   0% used [0x00000007bec80000, 0x00000007bec80000, 0x00000007becc0000)
         tenured generation   total 19456K, used 10507K [0x00000007bed00000, 0x00000007c0000000, 0x00000007c0000000)
         the space 19456K,  54% used [0x00000007bed00000, 0x00000007bf742d90, 0x00000007bf742e00, 0x00000007c0000000)
         Metaspace       used 3307K, capacity 4494K, committed 4864K, reserved 1056768K
         class space    used 369K, capacity 386K, committed 512K, reserved 1048576K
         */


        //第二次配置
        //-Xms20m -Xmx20m -Xmn7m -XX:SurvivorRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC
        //      -XX:SurvivorRatio=2 ： 表示 Eden ：from ：to = 2 ：1 ：1；
        //      新生代：7M； eden ：4/7M； from ：1/7M； to ：1/7M
        //      老年代：13M
        /**
         *  GC 打印日志如下：
         [GC (Allocation Failure) [DefNew: 2819K->1428K(5376K), 0.0020547 secs] 2819K->1428K(18688K), 0.0020899 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 4591K->1024K(5376K), 0.0023940 secs] 4591K->1429K(18688K), 0.0024186 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 4180K->1025K(5376K), 0.0009249 secs] 4585K->1430K(18688K), 0.0009488 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         Heap
         def new generation   total 5376K, used 4212K [0x00000007bec00000, 0x00000007bf300000, 0x00000007bf300000)
            eden space 3584K,  88% used [0x00000007bec00000, 0x00000007bef1cd80, 0x00000007bef80000)
            from space 1792K,  57% used [0x00000007bf140000, 0x00000007bf240560, 0x00000007bf300000)
            to   space 1792K,   0% used [0x00000007bef80000, 0x00000007bef80000, 0x00000007bf140000)
         tenured generation   total 13312K, used 405K [0x00000007bf300000, 0x00000007c0000000, 0x00000007c0000000)
            the space 13312K,   3% used [0x00000007bf300000, 0x00000007bf365530, 0x00000007bf365600, 0x00000007c0000000)
         Metaspace       used 3260K, capacity 4494K, committed 4864K, reserved 1056768K
            class space    used 363K, capacity 386K, committed 512K, reserved 1048576K
         *
         */

        //第三次配置
        //-XX:NewRatio=老年代/新生代
        //-Xms20m -Xmx20m -XX:NewRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC
        //      NewRatio=2； 老年代 ：新生代 = 2 ：1；
        //      老年代：20/3*2M；  新生代：20/3M
        /**
         *
         [GC (Allocation Failure) [DefNew: 4934K->410K(6144K), 0.0054894 secs] 4934K->1434K(19840K), 0.0055365 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
         [GC (Allocation Failure) [DefNew: 5691K->0K(6144K), 0.0033021 secs] 6715K->2458K(19840K), 0.0033423 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
         Heap
         def new generation   total 6144K, used 2239K [0x00000007bec00000, 0x00000007bf2a0000, 0x00000007bf2a0000)
            eden space 5504K,  40% used [0x00000007bec00000, 0x00000007bee2fbc8, 0x00000007bf160000)
            from space 640K,   0% used [0x00000007bf160000, 0x00000007bf160140, 0x00000007bf200000)
            to   space 640K,   0% used [0x00000007bf200000, 0x00000007bf200000, 0x00000007bf2a0000)
         tenured generation   total 13696K, used 2458K [0x00000007bf2a0000, 0x00000007c0000000, 0x00000007c0000000)
            the space 13696K,  17% used [0x00000007bf2a0000, 0x00000007bf506858, 0x00000007bf506a00, 0x00000007c0000000)
         Metaspace       used 3289K, capacity 4494K, committed 4864K, reserved 1056768K
            class space    used 367K, capacity 386K, committed 512K, reserved 1048576K
         */

        byte[] b = null;
        //连续向系统申请10MB空间
        for(int i = 0 ; i <10; i ++){
            b = new byte[1*1024*1024];
        }


    }
}
