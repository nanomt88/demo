package com.nanomt88.demo.ack;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * 用来发送消息的 spout
 *
 *      在失败之后进行重试，成功之后执行ACK操作
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-02 7:16
 **/
public class MessageSpout implements IRichSpout{

    private static String[] subjects = new String[]{"python,php","java,javascript","hadoop,hbase","vb,vc","c,c++"};

    private SpoutOutputCollector collector = null;

    private int index = 0;

    /**
     *  初始化方法
     * @param conf
     * @param context
     * @param collector
     */
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    /**
     * 这是Spout类中最重要的一个方法。发射一个Tuple到Topology都是通过这个方法来实现的。调用此方法时，storm向spout发出请求，
     * 让spout发出元组（tuple）到输出器（ouput collector）。这种方法应该是非阻塞的，所以spout如果没有元组发出，这个
     * 方法应该返回。nextTuple、ack 和fail 都在spout任务的同一个线程中被循环调用。 当没有元组的发射时，应该让nextTuple
     * 睡眠一个很短的时间（如一毫秒），以免浪费太多的CPU。继承了BaseRichSpout后，不用实现close、 activate、
     * deactivate、 ack、 fail 和 getComponentConfiguration 方法，只关心最基本核心的部分。通常情况下
     * （Shell和事务型的除外），实现一个Spout，可以直接实现接口IRichSpout，如果不想写多余的代码，可以直接继承BaseRichSpout
     */
    @Override
    public void nextTuple() {
        if(index < subjects.length){
            collector.emit(new Values(subjects[index]), index);    //使用index设置为messageId，用来唯一标识这个数据
            index ++;
        }
    }

    /**
     * 成功处理tuple时回调的方法，通常情况下，此方法的实现是将消息队列中的消息移除，防止消息重放
     * @param msgId
     */
    @Override
    public void ack(Object msgId) {
        System.out.println("【消费成功！！！】 - messageId : " + msgId);
    }

    /**
     * 处理tuple失败时回调的方法，通常情况下，此方法的实现是将消息放回消息队列中然后在稍后时间里重放
     * @param msgId
     */
    @Override
    public void fail(Object msgId) {
        System.out.println("【消费失败，重试！！！】 - messageId : " + msgId);
        collector.emit(new Values(subjects[(int) msgId]), msgId);   //使用emit进行重新发送消息
        System.out.println("【消息重发成功！！！】 - messageId : " + msgId);
    }

    /**
     * 用于声明当前spout发送的Tuple中包含的字段(field)，和bolt中类似
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("subjects"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    /**
     * 在该spout将要关闭时调用。但是不保证其一定被调用，因为在集群中supervisor节点，可以使用kill -9来
     *  杀死worker进程。只有当Storm是在本地模式下运行，如果是发送停止命令，可以保证close的执行
     */
    @Override
    public void close() {
        System.out.println("==================> spout close");
    }

    @Override
    public void activate() {
        System.out.println("==================> spout activate");
    }

    @Override
    public void deactivate() {
        System.out.println("==================> spout deactivate");
    }
}
