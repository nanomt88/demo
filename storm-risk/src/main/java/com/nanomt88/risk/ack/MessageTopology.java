package com.nanomt88.risk.ack;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

/**
 * 用来测试 bolt处理数据失败的情况
 *
 *      测试spout中的ack 和 fail函数
 *
 *      注意：
 *          在bolt中处理时，不要把数据拆分成多个传递给后面的bolt处理，后面的bolt失败后重试时，spout重新发送消息就会出现重复消费的问题。
 *
 *          针对重复消费的问题，建议在消费端做幂等性判断； 或者不要将一个 数据拆分成后发送
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-02 21:07
 **/
public class MessageTopology {

    public static void main(String[] args) throws InterruptedException {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout", new MessageSpout());
        builder.setBolt("split-bolt", new SplitBolt()).shuffleGrouping("spout");
        builder.setBolt("write-bolt", new WriteBolt()).shuffleGrouping("split-bolt");

        //本地配置
        Config config = new Config();
        config.setDebug(false);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("message-top", config, builder.createTopology());
        Thread.sleep(10 * 1000);
        cluster.killTopology("message-top");
        cluster.shutdown();
    }
}
