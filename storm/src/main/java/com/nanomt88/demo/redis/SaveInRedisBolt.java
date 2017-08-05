package com.nanomt88.demo.redis;

import com.nanomt88.demo.util.RedisOpsUtil;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 保存到redis的bolt
 *
 *      在实际运用中，一般不会直接保存到DB中，这样频繁入库对数据库压力太大，都是保存到redis中
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-05 22:32
 **/
public class SaveInRedisBolt implements IBasicBolt {

    private RedisOpsUtil redisOpsUtil;

    private String ip ;
    private int port ;

    public SaveInRedisBolt(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        redisOpsUtil = new RedisOpsUtil(ip, port);
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        Map<String, Object> record = new HashMap<>();
        String firstName = tuple.getStringByField("firstName");
        String lastName = tuple.getStringByField("lastName");
        String company = tuple.getStringByField("company");
        record.put("firstName", firstName);
        record.put("lastName", lastName);
        record.put("company", company);

        redisOpsUtil.set(UUID.randomUUID().toString(), record);
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
