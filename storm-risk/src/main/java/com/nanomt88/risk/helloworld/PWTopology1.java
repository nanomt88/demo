package com.nanomt88.risk.helloworld;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

/**
 * Topology运行流程
 *
 *     用于定义 spount 和 bolt的流程
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-31 8:24
 **/
public class PWTopology1 {

    public static void main(String[] args) throws InterruptedException, InvalidTopologyException,
            AuthorizationException, AlreadyAliveException {

        Config config = new Config();
        config.setNumWorkers(2);
        config.setDebug(true);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout", new PWSpout());
        builder.setBolt("print-bolt", new PrintBolt()).shuffleGrouping("spout");
        builder.setBolt("write-bolt", new WriterBolt()).shuffleGrouping("print-bolt");

        //1. 本地模式运行
//        LocalCluster cluster = new LocalCluster();
//        cluster.submitTopology("top1", config, builder.createTopology());
//        Thread.sleep(10 * 1000);
//        cluster.killTopology("top1");
//        cluster.shutdown();

        //2. 集群模式运行
        StormSubmitter.submitTopology("top1",config, builder.createTopology());
    }

}
