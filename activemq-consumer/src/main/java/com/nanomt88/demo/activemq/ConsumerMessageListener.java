package com.nanomt88.demo.activemq;

import com.alibaba.fastjson.JSON;
import com.nanomt88.demo.dto.MailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * Created by ZBOOK-17 on 2017/5/29.
 */
@Component
public class ConsumerMessageListener implements SessionAwareMessageListener<TextMessage>{

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination mailQueue;

    @Override
    public void onMessage(TextMessage message, Session session) throws JMSException {
        try {
            final String msg = message.getText();
            System.out.println("收到消息："+msg);

            MailMessage email = JSON.parseObject(msg, MailMessage.class);
            if(email == null){
                return;
            }
            //执行发送业务
            try{
                //执行业务 。。。。

            }catch (Exception e){
                // 发送异常，重新放回队列
				jmsTemplate.send(mailQueue, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(msg);
					}
				});
            }

        }catch (Exception e){


        }

    }
}
