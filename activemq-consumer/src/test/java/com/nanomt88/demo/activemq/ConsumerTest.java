package com.nanomt88.demo.activemq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ZBOOK-17 on 2017/5/30.
 */
public class ConsumerTest {

    public static void main(String[] args) throws InterruptedException {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "spring-context.xml" });
            context.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(1000*60*10);
    }
}