package com.nanomt88.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

public class MainTest {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"dubbo-provider.xml"});
        context.start();

        System.in.read(); // 按任意键退出
    }
}