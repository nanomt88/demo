package com.nanomt88.risk.trident.count;

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
import org.apache.storm.trident.operation.builtin.Count;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * 统计单词的  topology
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-05 16:04
 **/
public class WordCountTopology {

    public static StormTopology buildTopology(){
        TridentTopology topology = new TridentTopology();
        //设定数据源
        FixedBatchSpout spout = new FixedBatchSpout(
                //声明输入的域字段为 subjects
                new Fields("subjects"),
                //设置并行度为4： 即一次发送四条数据
                4,
                //设置数据源内容 ： 测试数据
                new Values("java java c++ c python ruby"),
                new Values("python ruby ruby c c++"),
                new Values("c++ c ruby java python"),
                new Values("c java python c c++"));
        //指定是否循环
        spout.setCycle(false);
        //指定输入源 spout
        Stream inputStream = topology.newStream("spout", spout);
        /**
         * 要实现流spout - bolt 的模式，在trident里面是使用each来做的
         *      each方法参数：
         *          1. 输入数据源参数名称： subjects
         *          2. 需要执行的function对象（也就是bolt对象）：new SplitFunction()
         *          3. 指定function对象里面的输出参数名次 ： sub
         */
        inputStream.shuffle()
                .each(new Fields("subjects"), new SplitFunction(),new Fields("sub"))
                //进行分组操作：参数为分组字段subject；比较类似接触的 fieldsGroup
                .groupBy(new Fields("sub"))
                //对分组之后的结果进行聚合操作:参数1为聚合方法为count函数，输出字段名称为count
                .aggregate(new Count(), new Fields("count"))
                /**
                 * 继续使用each调用下一个 function(bolt)
                 *      第一个输入参数为： "sub","count"
                 *      第二个参数为：new ResultFunction()也就是执行函数，输出内容
                 *      第三个参数为没有输出
                 */
                .each(new Fields("sub","count"), new ResultFunction(), new Fields());

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
