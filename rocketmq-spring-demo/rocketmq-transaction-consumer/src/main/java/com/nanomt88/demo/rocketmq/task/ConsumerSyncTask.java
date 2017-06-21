package com.nanomt88.demo.rocketmq.task;

import com.nanomt88.demo.rocketmq.entity.EventConsumerTask;
import com.nanomt88.demo.rocketmq.service.BalanceService;
import com.nanomt88.demo.rocketmq.service.EventConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 *  同步消费端已经消费过得任务列表，发送到producer端进行对比，将producer端未发送的任务进行再次发送
 * @author nanomt88@gmail.com
 * @create 2017-06-21 7:27
 **/
@Component
public class ConsumerSyncTask {

    @Autowired
    private Environment env;

    @Autowired
    EventConsumerService eventConsumerService;

    @Scheduled(cron = "0 */1 * * * ?")
    public void run(){
        EventConsumerTask task = eventConsumerService.getEventConsumerTask(env.getProperty("rocketmq.topic"));
        if(task == null){
            task = new EventConsumerTask();
            task.setTopic(env.getProperty("rocketmq.topic"));
            task.setUpdateTime(new Date(0));
        }
        eventConsumerService.syncConsumerEventMessages(task);

    }
}
