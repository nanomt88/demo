package com.nanomt88.risk.helloworld;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * Topology运行流程
 *
 *     用于定义 spount 和 bolt的流程
 *
 *     	a. 工作进程（worker processes）： 拓扑在集群中运行所需要的工作进程数 ——   相当于一个JVM进程
 *     	b. 执行器（executors）： 每个组件需要的执行线程数，一个 executor 是由 worker 进程生成的一个线程。
 *     	    在 executor 中可能会有一个或者多个 task，这些 task 都是为同一个组件（spout 或者 bolt）服务的。
 *     	c. 任务（tasks）： 每个组件需要的执行任务数，task 是实际执行数据处理的最小工作单元（注意，task 并不是线程，类似于设置批量处理个数）
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-31 8:24
 **/
public class PWTopology3 {

    public static void main(String[] args) throws InterruptedException, InvalidTopologyException,
            AuthorizationException, AlreadyAliveException {

        Config config = new Config();
        config.setNumWorkers(2);    //设置使用两个工作线程（可以理解为一个JVM进程）
        config.setDebug(true);

        TopologyBuilder builder = new TopologyBuilder();
        //设置spout的并行度和任务数（产生4个执行器）
        builder.setSpout("spout", new PWSpout(),4);
        //设置bolt的并行度和任务数:（产生4个执行器（executor））
        //注意：从 Storm 0.8 开始 parallelism_hint 参数代表 executor 的数量，而不是 task 的数量
        //      executor可以理解为java中的线程
        //      如果你在设置 bolt 的时候不指定 task 的数量，那么每个 executor 的 task 数会默认设置为 1。
        builder.setBolt("print-bolt", new PrintBolt(),4).shuffleGrouping("spout");
        //设置bolt的并行度和任务数:（产生6个执行器和6个任务，如果不设置任务数，则默认是一个executor一个task任务）
        //fieldsGrouping ： 设置根据field字段进行分组
//        builder.setBolt("write-bolt", new WriterBolt(),8).fieldsGrouping("print-bolt", new Fields("write"));

        // 也可以设置为 shuffleGrouping 随机分组
//        builder.setBolt("write-bolt", new WriterBolt(),6).shuffleGrouping("print-bolt");

        //设置广播分组 ： 所有的执行器都会收到相同的tuple ，类似于广播后所有节点都收到消息
//        builder.setBolt("write-bolt", new WriterBolt(), 4).allGrouping("print-bolt");
        //设置全局分组
        builder.setBolt("write-bolt", new WriterBolt(), 4).globalGrouping("print-bolt");


        //1. 本地模式运行
//        LocalCluster cluster = new LocalCluster();
//        cluster.submitTopology("top3", config, builder.createTopology());
//        Thread.sleep(10 * 1000);
//        cluster.killTopology("top3");
//        cluster.shutdown();

        //2. 集群模式运行
        StormSubmitter.submitTopology("top3",config, builder.createTopology());
    }

}
