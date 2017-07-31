package com.nanomt88.demo.helloworld;


import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 *  打印单词的bolt
 *
 *      Storm提供了IBasicBolt接口，其目的就是实现该接口的Bolt不用在代码中提供反馈结果了，Storm内部会自动反馈成功。
 *      如果你确实要反馈失败，可以抛出FailedException 通常情况下，实现一个Bolt，可以实现IRichBolt接口或继承
 *      BaseRichBolt。如果不想自己处理结果反馈，可以实现 IBasicBolt接口或继承BaseBasicBolt，它实际上相当于
 *      自动实现了collector.emit.ack(inputTuple)
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-31 6:56
 **/
public class PrintBolt extends BaseBasicBolt {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintBolt.class);

    /**
     *   用于声明当前Bolt发送的Tuple中包含的字段(field)，和Spout中类似
     *
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //进行声明， 声明的这个 print后，后面的bolt会接收叫 “write”的 字段后进行处理
        declarer.declare(new Fields("write"));
    }

    /**
     *  这是Bolt中最关键的一个方法，对于Tuple的处理都可以放到此方法中进行。具体的发送是通过emit方法来完成的。
     *   execute接受一个 tuple进行处理，并用prepare方法传入的OutputCollector的ack方法（表示成功）
     *   或fail（表示失败）来反馈处理结果。
     *
     * @param input
     * @param collector
     */
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //获取上一个组件所声明的Field
        String print = input.getStringByField("print");

        LOGGER.info("[print]: {}", print);
        //进行传递给下一个bolt
        collector.emit(new Values(print));
    }
}
