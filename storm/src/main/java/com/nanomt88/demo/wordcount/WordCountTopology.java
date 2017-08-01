package com.nanomt88.demo.wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * 统计句子中单词的个数
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-01 8:57
 **/
public class WordCountTopology {

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException, InterruptedException {

        TopologyBuilder builder = new TopologyBuilder();
        //设置spout的并行度和任务数（产生1个执行器和1个任务）
        builder.setSpout("spout", new SentenceSpout());
        //设置bolt的并行度和任务数:（产生5个执行器（executor）和 5个任务（Task  ））
        //注意：从 Storm 0.8 开始 parallelism_hint 参数代表 executor 的数量，而不是 task 的数量
        //executor可以理解为java中的线程
        //如果你在设置 bolt 的时候不指定 task 的数量，那么每个 executor 的 task 数会默认设置为 1。
        builder.setBolt("split-bolt", new SplitWordBolt(),5).shuffleGrouping("spout");
        //设置bolt的并行度和任务数:（产生5个执行器和5个任务，如果不设置任务数，则默认是一个executor一个task任务）
        //fieldsGrouping ： 设置根据field字段进行分组
        builder.setBolt("count-bolt", new CountBolt(),5).fieldsGrouping("split-bolt", new Fields("word"));
        builder.setBolt("report-bolt", new ReportBolt(),1).globalGrouping("count-bolt");


        Config config = new Config();
        config.setNumWorkers(2);    //设置使用两个工作线程（可以理解为一个JVM进程）
        config.setDebug(false);

        //1. 本地模式运行
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("word-count-top", config, builder.createTopology());
        Thread.sleep(10 * 1000);
        cluster.killTopology("word-count-top");
        cluster.shutdown();

        //2. 集群模式运行
//        StormSubmitter.submitTopology("word-count-top",config, builder.createTopology());
    }
}
