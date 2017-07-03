package com.nanomt88.common.util.asm;


import java.math.BigInteger;
import java.util.Random;

import static com.nanomt88.common.util.asm.SizeOfObject.*;

/**
 *  先使用mvn clean package打包
 *  然后到target目录下面，运行指定的jar包，命令如下：java -javaagent:test.jar.jar -cp . com.nanomt88.common.util.asm
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-01 9:19
 **/
public class TestObject {

    public static void main(String[] args) {


        BigInteger integer = new BigInteger(Long.MAX_VALUE+"");

        System.out.println("BigInteger object size:" + sizeOf(integer));

        BigInteger[] array = new BigInteger[5000];

        System.out.println("BigInteger array size:" + sizeOf(array));

        Random random = new Random();
        for (int i = 0; i < 5000; i++) {
            array[i] = new BigInteger( "9222222222222222222");
        }
        System.out.println("BigInteger array full size:" + sizeOf(array));

    }
}
