package com.nanomt88.demo.redis;

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
 * 用来给redis bolt 发射数据的 spout
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-05 22:19
 **/
public class SampleSpout extends BaseRichSpout {

    private static final long serialVersionUID = -6806106915312353468L;

    private static final Map<Integer, String> FIRST_NAME = new HashMap<Integer, String>();
    static {
        FIRST_NAME.put(0, "john");
        FIRST_NAME.put(1, "nick");
        FIRST_NAME.put(2, "mick");
        FIRST_NAME.put(3, "tom");
        FIRST_NAME.put(4, "jerry");
    }

    private static final Map<Integer, String> LAST_NAME = new HashMap<Integer, String>();
    static {
        LAST_NAME.put(0, "anderson");
        LAST_NAME.put(1, "watson");
        LAST_NAME.put(2, "ponting");
        LAST_NAME.put(3, "dravid");
        LAST_NAME.put(4, "lara");
    }

    private static final Map<Integer, String> COMPANY_NAME = new HashMap<>();

    static {
        COMPANY_NAME.put(0, "Huawei");
        COMPANY_NAME.put(1, "Google");
        COMPANY_NAME.put(2, "Microsoft");
        COMPANY_NAME.put(3, "Apple");
        COMPANY_NAME.put(4, "Amazon");
    }

    private SpoutOutputCollector collector;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        Random random = new Random();
        int index = random.nextInt(COMPANY_NAME.size());
        collector.emit(new Values(FIRST_NAME.get(index), LAST_NAME.get(index), COMPANY_NAME.get(index)));

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("firstName","lastName","company"));
    }
}
