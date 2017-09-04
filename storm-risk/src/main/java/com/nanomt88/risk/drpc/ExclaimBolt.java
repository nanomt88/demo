package com.nanomt88.risk.drpc;

import org.apache.storm.coordination.IBatchBolt;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 *    将传递进来的参数 添加一个 “！” 后返回
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-03 7:29
 **/
public class ExclaimBolt extends BaseBasicBolt {

    /**
     * 将进入的单词添加一个 “！” 后输出
     * @param tuple
     * @param collector
     */
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        String input = tuple.getString(1);
        collector.emit(new Values(tuple.getValue(0), input + "!"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "result"));
    }

}