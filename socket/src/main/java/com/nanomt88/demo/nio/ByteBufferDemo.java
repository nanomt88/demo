package com.nanomt88.demo.nio;

import java.nio.IntBuffer;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/4/15 下午4:13
 * @Description: //TODO
 */

public class ByteBufferDemo {

    public static void main(String[] args) {

        m1();
        //m2();
        //m3();
    }

    public static void m1(){
        IntBuffer buffer = IntBuffer.allocate(10);
        buffer.put(11); // position位置：0 - > 1
        buffer.put(22); // position位置：1 - > 2
        buffer.put(33); // position位置：2 - > 3


        //把位置复位到0，也就是position位置从：3 ——> 0
        System.out.println("flip()复位之前：" + buffer);
        buffer.flip();  //flip()之后，limit就不能修改，容量就固定了


        System.out.println("flip()复位之后：" + buffer);
        System.out.println("position位置为: " + buffer.position());	//容量一旦初始化后不允许改变（warp方法包裹数组除外）
        System.out.println("capacity容量为: " + buffer.capacity());	//容量一旦初始化后不允许改变（warp方法包裹数组除外）
        System.out.println("limit上限为: " + buffer.limit());		//由于只装载了三个元素,所以可读取或者操作的元素为3 则limit=3
        System.out.println("获取下标为1的元素：" + buffer.get(1));
        System.out.println("get(index)方法，position位置不改变：" + buffer);


        buffer.put(1, 4);

        for (int i = 0; i < buffer.limit(); i++) {
            System.out.println(buffer.get()+"\t");
        }
        System.out.println("buffer对象遍历之后：" + buffer);

        //可以重新调用limit 设置上限容量
        buffer = (IntBuffer) buffer.limit(10);
        buffer.put(6,32);
        System.out.println(buffer);

    }

    /**
     *  2 wrap方法使用
     */
    public static void m2() {

         //  wrap方法会包裹一个数组: 一般这种用法不会先初始化缓存对象的长度，因为没有意义，最后还会被wrap所包裹的数组覆盖掉。
         //  并且wrap方法修改缓冲区对象的时候，数组本身也会跟着发生变化。
         int[] arr = new int[]{1,2,5};
         IntBuffer buf1 = IntBuffer.wrap(arr);
         System.out.println(buf1);
        for (int i = 0; i < buf1.limit(); i++) {
            System.out.print(buf1.get()+"\t");
        }
        System.out.println();

         IntBuffer buf2 = IntBuffer.wrap(arr, 0 , 2);
         //这样使用表示容量为数组arr的长度，但是可操作的元素只有实际进入缓存区的元素长度
         System.out.println(buf2);

        for (int i = 0; i < buf2.limit(); i++) {
            System.out.print(buf2.get()+"\t");
        }

    }

    public static void m3() {
        IntBuffer buf1 = IntBuffer.allocate(10);

        int[] arr = new int[]{1,2,5};
        //设置数据
        buf1.put(arr);
        System.out.println(buf1 +" : "+ buf1.array());

        //第一种复制方法
        IntBuffer buf2 = buf1.duplicate();
        System.out.println(buf2 +" : "+ buf2.array());

        //buf1.position(0);
        buf1.flip();
        System.out.println(buf1);

        System.out.println("可读数据为：" + buf1.remaining());
        int[] arr2 = new int[buf1.remaining()];
        buf1.get(arr2);
        for (int i : arr2) {
            System.out.print(Integer.toString(i) + ",");
        }
    }
}
