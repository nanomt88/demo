package com.nanomt88.risk.trident.strategy;

import com.nanomt88.risk.trident.sample.TridentFunction;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * 测试各自分组策略的demo
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-05 9:23
 **/
public class StrategyTopology {

    public static StormTopology buildTopology(){
        TridentTopology topology = new TridentTopology();
        //设定数据源
        FixedBatchSpout spout = new FixedBatchSpout(
                //声明输入的域字段为 sub
                new Fields("sub"),
                //设置并行度为4： 即一次发送四条数据，只发送一次
                4,
                //设置数据源内容 ： 测试数据
                new Values("java"),
                new Values("python"),
                new Values("php"),
                new Values("ruby"),
                new Values("c++"));
        //指定是否循环
        spout.setCycle(true);
        //指定输入源 spout
        Stream inputStream = topology.newStream("spout", spout);

        inputStream
                //随机分组 ： shuffle
//                .shuffle()
                //分区分组 ：partitionBy ,按照指定的sub字段 进行分组；跟bolt中fieldsGrouping一样
//                .partitionBy(new Fields("sub"))
                //全局分组 ： global , 所有的消息都要走这一个function；跟bolt中 globalGrouping一样
//                .global()
                //广播分支： broadcast，所有的组都接收到这个消息；跟bolt中allGrouping一样
                .broadcast()
        /**
         * 要实现流spout - bolt 的模式，在trident里面是使用each来做的
         *      each方法参数：
         *          1. 输入数据源参数名称： "sub"
         *          2. 需要执行的function对象（也就是bolt对象）：new WriteFunction()
         *          3. 指定function对象里的输出参数名称,没有则不输出任何内容
         */
                .each(new Fields("sub"), new WriteFunction(),new Fields())
                //设置并行度 : 并行度为4，就会有4个executor执行，在随机分组的情况下：就会生成4个文件
                .parallelismHint(4);

        return topology.build();    //使用这种方式，返回一个StormTopology对象进行提交
    }

    public static void main(String[] args) throws InterruptedException, InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        Config config = new Config();
        //设置batch最大处理work数量
        config.setNumWorkers(2);
        config.setMaxSpoutPending(20);
        if(args == null || args.length == 0){
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("group-function", config, buildTopology());
            Thread.sleep(10 * 1000);
            cluster.shutdown();
        }else {
            StormSubmitter.submitTopology(args[0], config, buildTopology());
        }
    }
}
