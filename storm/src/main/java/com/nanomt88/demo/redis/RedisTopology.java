package com.nanomt88.demo.redis;

import com.nanomt88.demo.helloworld.PWSpout;
import com.nanomt88.demo.helloworld.PrintBolt;
import com.nanomt88.demo.helloworld.WriterBolt;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * 集成redis的 topology demo
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-05 22:47
 **/
public class RedisTopology {

    public static void main(String[] args) throws InterruptedException {

        Config config = new Config();
        config.setNumWorkers(2);    //设置使用两个工作线程（可以理解为一个JVM进程）
        config.setDebug(false);

        TopologyBuilder builder = new TopologyBuilder();
        //设置spout的并行度和任务数（产生2个执行器和两个任务）
        builder.setSpout("spout", new SampleSpout(),2);
        //设置bolt的并行度和任务数:（产生2个执行器（executor）和 4个任务（Task  ））
        //注意：从 Storm 0.8 开始 parallelism_hint 参数代表 executor 的数量，而不是 task 的数量
        //executor可以理解为java中的线程
        //如果你在设置 bolt 的时候不指定 task 的数量，那么每个 executor 的 task 数会默认设置为 1。
        builder.setBolt("redis-bolt", new SaveInRedisBolt("127.0.0.1", 6379),2).shuffleGrouping("spout");

        //1. 本地模式运行
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("redis-top", config, builder.createTopology());

        Thread.sleep(10 * 1000);

        cluster.killTopology("redis-top");
        cluster.shutdown();

    }
}
