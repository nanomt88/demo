package com.nanomt88.demo.wordcount;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 *  拆分单词后发射给下一个bolt 进行统计计数
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-01 7:49
 **/
public class SplitWordBolt extends BaseBasicBolt {


    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //后面的bolt 接收 “word” 这个字段后，进行处理
        declarer.declare(new Fields("word"));
    }

    /**
     * 拆分单词后发射给下一个bolt
     * @param input
     * @param collector
     */
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String sentences = input.getStringByField("sentences");
        String[] words = sentences.split(" ");
        for (String word : words){
            collector.emit(new Values(word));
        }
    }
}
