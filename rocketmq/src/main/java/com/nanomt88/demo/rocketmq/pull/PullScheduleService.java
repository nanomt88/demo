package com.nanomt88.demo.rocketmq.pull;/**
 * Created by ZBOOK-17 on 2017/6/18.
 */

import org.apache.rocketmq.client.consumer.*;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

/**
 *  rocket pull 方式 demo
 *      注意： pull 拉消息的时候，是多线程并发拉取，每个线程从一个队列中拉取，如果每个broke中有四个，则两个broker有八个线程同时拉取
 *      2. 对于Push而言，不论是基于MessageListenerConcurrently的，还是基于MessageListenerOrderly的，都有返回值的；而Pull的doPullTask的返回值却是void？
 *      这意味，我们需要在pull方式中，注意自己处理每条消息消费的异常情况！
 *
 * @author nanomt88@gmail.com
 * @create 2017-06-18 18:23
 **/
public class PullScheduleService {

    private static final String groupName = "scheduleGroup";
    private static final String topicName = "pullTopic2";

    public static void main(String[] args) throws InterruptedException, MQClientException {

        final MQPullConsumerScheduleService scheduleService = new MQPullConsumerScheduleService(groupName);
        scheduleService.getDefaultMQPullConsumer().setNamesrvAddr("192.168.1.140:9876;192.168.1.141:9876");
        scheduleService.setMessageModel(MessageModel.CLUSTERING);

        scheduleService.registerPullTaskCallback(topicName, new PullTaskCallback() {
            @Override
            public void doPullTask(MessageQueue mq, PullTaskContext context) {
                MQPullConsumer consumer = context.getPullConsumer();

                System.out.println(Thread.currentThread().getName()+"  =============> 拉取数据 ["+ mq.getBrokerName()+"-"+ mq.getQueueId() +"] 开始。。。。");

                try {
                    //从哪里拉取数据
                    long offset = consumer.fetchConsumeOffset(mq, false);

                    if(offset < 0){
                        offset = 0;
                    }

                    //拉取数据，maxNums : 一次拉取多少条数据
                    PullResult result = consumer.pull(mq, "*", offset, 4);
                    switch (result.getPullStatus()){
                        case FOUND:
                            try {
                                //拉取到数据之后，进行消费
                                List<MessageExt> list = result.getMsgFoundList();
                                for (MessageExt msg : list) {
                                    System.out.println(Thread.currentThread().getName() + "  [" + mq.getBrokerName() + "-" + mq.getQueueId() + "]  key:" + msg.getKeys() + "  msg_body :" + new String(msg.getBody()));
                                }
                            }catch (Exception e){
                                //因为pull这没有返回值，出现异常的情况下需要自己处理异常逻辑，不能抛出异常
                            }
                            break;
                        case NO_NEW_MSG:
                           ;
                        case NO_MATCHED_MSG:
                            break;
                        case OFFSET_ILLEGAL:
                            break;
                        default:
                            break;
                    }

                    // 这个厉害了！！！
                    // 每次拉取完了之后，需要存储offset到mq （客户端每隔5秒会定时刷新到broker）
                    // 不要超过5秒
                    consumer.updateConsumeOffset(mq, result.getNextBeginOffset());

                    // 设置再过多久之后重新拉取消息： 10秒
                    context.setPullNextDelayTimeMillis(6 * 1000);

                } catch (MQClientException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemotingException e) {
                    e.printStackTrace();
                } catch (MQBrokerException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName()+"  =============> 拉取数据 ["+mq.getBrokerName()+"-"+ mq.getQueueId() +"] 结束。。。。");
            }
        });
        scheduleService.start();
    }
}
