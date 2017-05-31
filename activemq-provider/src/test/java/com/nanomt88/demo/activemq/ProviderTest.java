package com.nanomt88.demo.activemq;

import com.nanomt88.demo.dto.MailMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by ZBOOK-17 on 2017/5/30.
 */
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ProviderTest {

    @Autowired
    Provider provider;

    @Test
    public void sendEmail() {

        MailMessage msg = new MailMessage();
        msg.setContent("from active demo");
        msg.setFrom("xuelang0734@126.com");
        msg.setTo("87707918@qq.com");
        msg.setContent("demo test mail");
        provider.sendEmail(msg);
    }

}