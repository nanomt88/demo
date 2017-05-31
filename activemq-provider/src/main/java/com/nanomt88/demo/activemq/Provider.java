package com.nanomt88.demo.activemq;

import com.alibaba.fastjson.JSONObject;
import com.nanomt88.demo.dto.MailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Created by ZBOOK-17 on 2017/5/29.
 */
@Service
public class Provider {

    @Autowired
    JmsTemplate jmsTemplate;

    public void sendEmail(final MailMessage msg){

        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                String json = JSONObject.toJSONString(msg);
                System.out.println("发送消息：：：：" + json);
                return session.createTextMessage(json);
            }
        });

    }
}
