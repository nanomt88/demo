package com.nanomt88.common.util.asm;


import java.math.BigInteger;
import java.util.Random;

import static com.nanomt88.common.util.asm.SizeOfObject.*;

/**
 *  添加maven打包配置，如下：
 *
 *      打jar相关的配置
         <plugin>
         <groupId>org.apache.maven.plugins</groupId>
         <artifactId>maven-jar-plugin</artifactId>
         <version>2.4</version>
         <configuration>
         <finalName>test</finalName>
         <archive>
         <manifest>
         <addClasspath>true</addClasspath>
         <classpathPrefix>lib/</classpathPrefix>
         <mainClass>com.nanomt88.common.util.asm.TestObject</mainClass>
         </manifest>
         <manifestEntries>
         <Premain-class>com.nanomt88.common.util.asm.SizeOfObject</Premain-class>
         <Boot-Class-Path></Boot-Class-Path>
         <Can-Redefine-Classes>false</Can-Redefine-Classes>
         </manifestEntries>
         <addMavenDescriptor>false</addMavenDescriptor>
         </archive>
         </configuration>
         </plugin>
 *
 *  先使用mvn clean package打包
 *  然后到target目录下面，运行指定的jar包，命令如下：
 *          java -javaagent:test.jar -cp . com.nanomt88.common.util.asm.TestObject
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
