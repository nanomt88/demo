package com.nanomt88.risk;

import com.alibaba.fastjson.JSONObject;
import com.nanomt88.risk.util.RedisOpsUtil;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author nanomt88@gmail.com
 * @create 2017-09-08 7:34
 **/
public class MerchantRiskConfigBolt extends BaseBasicBolt {

    public static final String CACHE_MERCHANT_RULE = "MERCHANT_RULE_";

    private RedisOpsUtil redisOpsUtil;
    private Map<String, List<String>> ruleMap ;

    private String ip ;
    private int port ;

    public MerchantRiskConfigBolt(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        redisOpsUtil = new RedisOpsUtil(ip, port);
        ruleMap = new ConcurrentHashMap<>();
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector)  {
        String json = tuple.getString(1);
        Order order = JSONObject.parseObject(json,Order.class);

        List<String> rules = ruleMap.get(order.getMerchantId());
        if( rules == null){
            //从缓存加载规则
            rules = loadRule4Redis(order.getMerchantId());
            //规则为空则为风控拒绝
            if(rules == null){
                order.setRiskCheckResult(RiskStatus.REJECTED, RiskStatus.MISSING_RISK_RULE);
                collector.emit(new Values(order, null));
                return;
            }
        }
        //并行发送
        for(String rule : rules) {
            collector.emit(new Values(order, rule));
        }
    }

    /**
     * 从缓存中加载风控规则
     * @param merchantId
     * @return
     */
    private List<String> loadRule4Redis(String merchantId) {
        List<String> list = redisOpsUtil.get(CACHE_MERCHANT_RULE + merchantId, List.class);
        if(list == null){
            return null;
        }
        ruleMap.put(merchantId, list);
        return list;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
