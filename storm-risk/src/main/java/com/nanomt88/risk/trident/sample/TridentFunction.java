package com.nanomt88.risk.trident.sample;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * 使用 Trident 方式提到 spout 和 bolt
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-04 8:31
 **/
public class TridentFunction {

    /**
     * 继承 BaseFunction类，重写 execute方法
     *       将域a、b 的值相加之后发射给下一个functions
     */
    public static class SumFunction extends BaseFunction{
        @Override
        public void execute(TridentTuple tuple, TridentCollector collector) {
            //获取 a / b 两个域
            System.out.println("------------------------------");
            System.out.println("SumFunction 入参：" + tuple);
            int a = tuple.getInteger(0);
            int b = tuple.getInteger(1);
            //发射数据
            int sum = a + b ;
            collector.emit(new Values(sum));
        }
    }

    /**
     *  继承 BaseFunction类，重写 execute方法
     *       将域a、b 的值相加之后发射给下一个functions
     */
    public static class Result extends BaseFunction {
        @Override
        public void execute(TridentTuple tuple, TridentCollector collector) {
            //获取 tuple输入内容
            Integer a = tuple.getIntegerByField("a");
            Integer b = tuple.getIntegerByField("b");
            Integer c = tuple.getIntegerByField("c");
            Integer d = tuple.getIntegerByField("d");

            System.out.println("a : " + a + " , b : " + b + " , c : " + c + " , d : " + d );

            Integer sum = tuple.getIntegerByField("sum");
            System.out.println("sum : a + b = " + sum);

            System.out.println();
        }
    }

    public static StormTopology buildTopology(){
        TridentTopology topology = new TridentTopology();
        //设定数据源
        FixedBatchSpout spout = new FixedBatchSpout(
                //声明输入的域字段为 a 、b 、 c、 d
                new Fields("a" , "b", "c" , "d"),
                //设置并行度为4： 即一次发送四条数据
                4,
                //设置数据源内容 ： 测试数据
                new Values(1,4,7,10),
                new Values(1,1,3,11),
                new Values(2,3,7,2),
                new Values(2,5,8,2));
        //指定是否循环
        spout.setCycle(false);
        //指定输入源 spout
        Stream inputStream = topology.newStream("spout", spout);
        /**
         * 要实现流spout - bolt 的模式，在trident里面是使用each来做的
         *      each方法参数：
         *          1. 输入数据源参数名称： "a" , "b", "c" , "d"
         *          2. 需要执行的function对象（也就是bolt对象）：new SumFunction()
         *          3. 指定function对象里面的输出参数名次 ： sum
         */
        inputStream.each(new Fields("a" , "b", "c" , "d"), new SumFunction(),new Fields("sum"))
                /**
                 * 继续使用each调用下一个 function(bolt)
                 *      第一个输入参数为： "a" , "b", "c" , "d", "sum"
                 *      第二个参数为：new Result()也就是执行函数，第三个参数为没有输出
                 */
                .each(new Fields("a" , "b", "c" , "d", "sum"), new Result(), new Fields());

        return topology.build();    //使用这种方式，返回一个StormTopology对象进行提交
    }

    public static void main(String[] args) throws InterruptedException, InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        Config config = new Config();
        //设置batch最大处理work数量
        config.setNumWorkers(2);
        config.setMaxSpoutPending(20);
        if(args == null || args.length == 0){
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("trident-function", config, buildTopology());
            Thread.sleep(10 * 1000);
            cluster.shutdown();
        }else {
            StormSubmitter.submitTopology(args[0], config, buildTopology());
        }
    }
}
