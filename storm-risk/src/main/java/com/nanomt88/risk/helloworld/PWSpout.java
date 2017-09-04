package com.nanomt88.risk.helloworld;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *    用来发射 单词给 print bolt 和 writer bolt
 *
 *      继承了BaseRichSpout后，不用实现close、 activate、 deactivate、 ack、 fail 和 getComponentConfiguration 方法，
 *      只关心最基本核心的部分。通常情况下（Shell和事务型的除外），实现一个Spout，可以直接实现接口IRichSpout，
 *      如果不想写多余的代码，可以直接继承BaseRichSpout
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-31 6:31
 **/
public class PWSpout extends BaseRichSpout{

    private static final long serialVersionUID = 4864508158156736343L;

    /**
     * 存储用来发射单词的map
     */
    private static Map<Integer,String> map = new HashMap<>();

    static {
        map.put(0, "java");
        map.put(1, "php");
        map.put(2, "groovy");
        map.put(3, "python");
        map.put(4, "ruby");
        map.put(5, "clojure");
    }

    private SpoutOutputCollector collector;

    /**
     * 初始化方法
     * @param map
     * @param topologyContext
     * @param spoutOutputCollector
     */
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        //对spout进行初始化
        this.collector = spoutOutputCollector;
        System.out.println("==================> init collector : " + this.collector.toString());
    }

    /**
     * 这是Spout类中最重要的一个方法。发射一个Tuple到Topology都是通过这个方法来实现的。调用此方法时，storm向spout发出请求，
     * 让spout发出元组（tuple）到输出器（ouput collector）。这种方法应该是非阻塞的，所以spout如果没有元组发出，这个方法应该返回。
     * nextTuple、ack 和fail 都在spout任务的同一个线程中被循环调用。 当没有元组的发射时，应该让nextTuple睡眠一个很短的
     * 时间（如一毫秒），以免浪费太多的CPU。
     */
    @Override
    public void nextTuple() {
        //随机发送一个单词
        final Random r = new Random();
        int num = r.nextInt(6);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 发射单词
        this.collector.emit(new Values(map.get(num)));
    }

    /**
     * 用于声明当前Spout发送的Tuple中包含的字段(field)，和Bolt中类似
     * @param outputFieldsDeclarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //进行声明， 声明的这个 spout后，后面的bolt会接收叫 “print”的 字段后进行处理
        outputFieldsDeclarer.declare(new Fields("print"));
    }
}
