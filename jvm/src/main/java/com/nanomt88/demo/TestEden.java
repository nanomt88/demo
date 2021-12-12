package com.nanomt88.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/5/7 下午11:11
 * @Description: //TODO
 */

public class TestEden {

    public static void main(String[] args) {
        //初始的对象在eden区
        //参数：-Xmx64M -Xms64M -XX:+PrintGCDetails
        /**   可以看到，5M的byte对象都 分配在eden区
         *Heap
         PSYoungGen      total 18944K, used 7753K [0x00000007beb00000, 0x00000007c0000000, 0x00000007c0000000)
            eden space 16384K, 47% used [0x00000007beb00000,0x00000007bf2924f0,0x00000007bfb00000)
            from space 2560K, 0% used [0x00000007bfd80000,0x00000007bfd80000,0x00000007c0000000)
            to   space 2560K, 0% used [0x00000007bfb00000,0x00000007bfb00000,0x00000007bfd80000)
         ParOldGen       total 44032K, used 0K [0x00000007bc000000, 0x00000007beb00000, 0x00000007beb00000)
            object space 44032K, 0% used [0x00000007bc000000,0x00000007bc000000,0x00000007beb00000)
         Metaspace       used 3305K, capacity 4494K, committed 4864K, reserved 1056768K
            class space    used 369K, capacity 386K, committed 512K, reserved 1048576K
         *
         */
//		for(int i=0; i< 5; i++){
//			byte[] b = new byte[1024*1024];
//		}


        //测试进入老年代的对象
        //
        //参数：-Xmx1024M -Xms1024M -XX:+UseSerialGC -XX:MaxTenuringThreshold=15 -XX:+PrintGCDetails
        //-XX:+PrintHeapAtGC

        /**
         *  MaxTenuringThreshold=15;会经历15次GC之后，对象从
         [GC (Allocation Failure) [DefNew: 278925K->5555K(314560K), 0.0064153 secs] 278925K->5555K(1013632K), 0.0064657 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
         [GC (Allocation Failure) [DefNew: 284391K->5541K(314560K), 0.0076410 secs] 284391K->5541K(1013632K), 0.0076658 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
         [GC (Allocation Failure) [DefNew: 284901K->5540K(314560K), 0.0022497 secs] 284901K->5540K(1013632K), 0.0022758 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 284428K->5540K(314560K), 0.0022243 secs] 284428K->5540K(1013632K), 0.0022568 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 284466K->5540K(314560K), 0.0044950 secs] 284466K->5540K(1013632K), 0.0045473 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
         [GC (Allocation Failure) [DefNew: 284491K->5540K(314560K), 0.0018613 secs] 284491K->5540K(1013632K), 0.0018904 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 284507K->5540K(314560K), 0.0018863 secs] 284507K->5540K(1013632K), 0.0019123 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 284517K->5540K(314560K), 0.0015043 secs] 284517K->5540K(1013632K), 0.0015254 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 284524K->5540K(314560K), 0.0018438 secs] 284524K->5540K(1013632K), 0.0018701 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 284528K->5540K(314560K), 0.0034981 secs] 284528K->5540K(1013632K), 0.0035251 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 284531K->5540K(314560K), 0.0017713 secs] 284531K->5540K(1013632K), 0.0018028 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 284532K->5540K(314560K), 0.0016032 secs] 284532K->5540K(1013632K), 0.0016261 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 284533K->5540K(314560K), 0.0018332 secs] 284533K->5540K(1013632K), 0.0018597 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 284534K->5540K(314560K), 0.0022383 secs] 284534K->5540K(1013632K), 0.0022887 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 284535K->5540K(314560K), 0.0015286 secs] 284535K->5540K(1013632K), 0.0015507 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 284535K->0K(314560K), 0.0042603 secs] 284535K->5540K(1013632K), 0.0042854 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
         [GC (Allocation Failure) [DefNew: 278994K->0K(314560K), 0.0002229 secs] 284535K->5540K(1013632K), 0.0002411 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 278994K->0K(314560K), 0.0003340 secs] 284535K->5540K(1013632K), 0.0003939 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 278995K->0K(314560K), 0.0002874 secs] 284535K->5540K(1013632K), 0.0003112 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 278995K->0K(314560K), 0.0006807 secs] 284535K->5540K(1013632K), 0.0007347 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 278995K->0K(314560K), 0.0002274 secs] 284535K->5540K(1013632K), 0.0002524 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [GC (Allocation Failure) [DefNew: 278995K->0K(314560K), 0.0002641 secs] 284535K->5540K(1013632K), 0.0002897 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         Heap
         def new generation   total 314560K, used 44968K [0x0000000780000000, 0x0000000795550000, 0x0000000795550000)
            eden space 279616K,  16% used [0x0000000780000000, 0x0000000782bea0d8, 0x0000000791110000)
            from space 34944K,   0% used [0x0000000791110000, 0x0000000791110000, 0x0000000793330000)
            to   space 34944K,   0% used [0x0000000793330000, 0x0000000793330000, 0x0000000795550000)
         tenured generation   total 699072K, used 5540K [0x0000000795550000, 0x00000007c0000000, 0x00000007c0000000)
            the space 699072K,   0% used [0x0000000795550000, 0x0000000795ab92c0, 0x0000000795ab9400, 0x00000007c0000000)
         Metaspace       used 3303K, capacity 4494K, committed 4864K, reserved 1056768K
            class space    used 369K, capacity 386K, committed 512K, reserved 1048576K
         */

		for(int k = 0; k<20; k++) {
			for(int j = 0; j<300; j++){
				byte[] b = new byte[1024*1024];
			}
		}

    }
}
