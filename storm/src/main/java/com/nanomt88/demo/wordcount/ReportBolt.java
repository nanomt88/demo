package com.nanomt88.demo.wordcount;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.util.*;

/**
 * 汇总所有统计的结果 ，并且输出
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-01 8:50
 **/
public class ReportBolt implements IRichBolt {

    private OutputCollector collector = null;

    private Map<String,Integer> map = null;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        map = new HashMap<>();
    }

    @Override
    public void execute(Tuple input) {
        String word = input.getStringByField("word");
        Integer count = input.getIntegerByField("count");
        map.put(word,count);
    }

    @Override
    public void cleanup() {
        System.out.println("----------------- Final result -----------------------");
        List<String> keys = new ArrayList<>();
        keys.addAll(map.keySet());
        Collections.sort(keys);
        for(String key : keys){
            System.out.println("word:" + key + "      count:" + map.get(key));
        }
        System.out.println("----------------- Final result -----------------------");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
