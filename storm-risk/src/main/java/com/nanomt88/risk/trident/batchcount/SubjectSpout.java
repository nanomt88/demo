package com.nanomt88.risk.trident.batchcount;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.spout.IBatchSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于批处理的spout
 *      建议一次发送一批数据，如果成功则删除本地缓存的数据；消费失败则重新发送这一批数据
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-05 16:55
 **/
public class SubjectSpout implements IBatchSpout {

    private static final long serialVersionUID = 4649138650522521406L;

    //批处理大小
    private int batchSize;

    //保存每次发送的批次 和 数据
    private Map<Long, List<List<Object>>> batchMap = new ConcurrentHashMap<>();

    private static final Map<Integer, String> DATA_MAP = new HashMap<>();

    static {
        DATA_MAP.put(0, "java java php ruby c++");
        DATA_MAP.put(1, "java python python python c++");
        DATA_MAP.put(2, "php c c++ c java ruby");
        DATA_MAP.put(3, "c++ java ruby php php");
    }

    public SubjectSpout(int batchSize){
        this.batchSize = batchSize;
    }

    @Override
    public void open(Map conf, TopologyContext context) {
    }

    @Override
    public void emitBatch(long batchId, TridentCollector collector) {
        //整个map的数据作为一批进行发送
        List<List<Object>> batches = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            batches.add(new Values(DATA_MAP.get(i)));
        }
        System.out.println("batchID : " + batchId);
        //将这一批数据缓存起来，如果出错就重发这一批数据，如果成功则删除
        batchMap.put(batchId, batches);
        for (List<Object> list : batches){
            collector.emit(list);
        }
    }

    @Override
    public void ack(long batchId) {
        System.out.println("batchId : " + batchId +" 消费成功， remove batchId");
        //消费成功之后就删除缓存的数据
        batchMap.remove(batchId);
    }

    @Override
    public Fields getOutputFields() {
        return new Fields("subjects");
    }

    @Override
    public void close() {
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
