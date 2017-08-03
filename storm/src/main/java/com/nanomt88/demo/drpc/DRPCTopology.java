package com.nanomt88.demo.drpc;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.StormSubmitter;
import org.apache.storm.drpc.LinearDRPCTopologyBuilder;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.junit.Assert;

/**
 * 简单的DRPC demo
 *
 *      向远程的bolt提交一个字符串，处理完成之后输出处理结果
 *
 *      如何创建并提交到远程bolt：
 *
 *          1. 配置并启动 DRPC 服务器； 启动drpc 服务：bin/storm drpc
 *                 编辑 storm.yaml ，添加如下配置：
 *                 drpc.servers:
                        - "drpc1.foo.com"
                        - "drpc2.foo.com"
 *          2. 在集群的各个服务器上配置 DRPC 服务器的地址；
 *                  编辑 storm.yaml ，添加如下配置：
 *                   drpc.servers:
                         - "drpc1.foo.com"
                         - "drpc2.foo.com"
 *          3. 将 DRPC 拓扑提交到集群运行。
 *                  storm jar xxx.jar com.nanomt88.demo.drpc.DRPCTopology exc
 *
 *
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-03 7:25
 **/
public class DRPCTopology {

    public static void main(String[] args) throws InterruptedException, InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("exclamation");
        builder.addBolt(new ExclaimBolt(), 3);

        Config config = new Config();

        //本地模式
        if(args == null || args.length == 0) {
            LocalDRPC drpc = new LocalDRPC();
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("drpc-demo", config, builder.createLocalTopology(drpc));

            Thread.sleep(1000 * 10);

            try {

                for (String word : new String[]{"hello", "world", "NB"}) {
                    System.out.println("before : " + word + "       after : " + drpc.execute("exclamation", word));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail(" failed to run DRPC demo");
            }

            drpc.shutdown();
            cluster.shutdown();

        }else { //集群模式
            config.setNumWorkers(3);
            StormSubmitter.submitTopology(args[0], config, builder.createRemoteTopology());
        }
    }
}
