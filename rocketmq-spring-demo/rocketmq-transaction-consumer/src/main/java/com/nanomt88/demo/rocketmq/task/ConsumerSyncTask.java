package com.nanomt88.demo.rocketmq.task;

import com.nanomt88.demo.rocketmq.service.EventConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 *  同步消费端已经消费过得任务列表，发送到producer端进行对比，将producer端未发送的任务进行再次发送
 * @author nanomt88@gmail.com
 * @create 2017-06-21 7:27
 **/
@Component
public class ConsumerSyncTask {

    @Autowired
    EventConsumerService eventConsumerService;

    @Scheduled(cron = "0 */1 * * * ?")
    public void run(){
        try {
            eventConsumerService.syncConsumerEventMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
