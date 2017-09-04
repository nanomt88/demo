package com.nanomt88.risk.ack;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.MessageId;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用来拆分 spout 发射过来的数据
 *
 *      测试数据处理失败之后，进行重发的示例
 *
 *      注意： 这里消费重试的时候，可能会出现消息重复消费的问题。针对重复消费的问题，建议在消费端做幂等性判断；
 *          或者不要将一个 数据拆分成后发送
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-02 7:43
 **/
public class SplitBolt implements IRichBolt{

    private OutputCollector collector;

    private boolean flag = true;

    /**
     *  此方法和Spout中的open方法类似，在集群中一个worker中的task初始化时调用。 它提供了bolt执行的环境
     * @param stormConf
     * @param context
     * @param collector
     */
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    /**
     * 这是Bolt中最关键的一个方法，对于Tuple的处理都可以放到此方法中进行。具体的发送是通过emit方法来完成的。
     * execute接受一个 tuple进行处理，并用prepare方法传入的OutputCollector的ack方法（表示成功）或fail（表示失败）
     * 来反馈处理结果。
     *
     *      注意：
     *          Storm提供了IBasicBolt接口，其目的就是实现该接口的Bolt不用在代码中提供反馈结果了，Storm内部会自动反馈成功。
     *          如果你确实要反馈失败，可以抛出FailedException
     * @param tuple
     */
    @Override
    public void execute(Tuple tuple) {

        try {
            String string = tuple.getString(0);

//            if ( flag && string.equals("vb,vc")) {
//                flag = false;
//                int i = 1 / 0;
//            }

            String[] words = string.split(",");

            //////////     错误示例     ////////////
            for(String word : words){
                //注意： 这里循环发送消息，需要携带tuple对象，用于处理异常时重发策略
                collector.emit(tuple, new Values(word));
            }
            //////////     错误示例     ////////////

            /**
            //////////     正确示例     ////////////
             // 尽量不要拆分数据，使用一次发送；否则在后面的bolt处理失败后重试，会导致部分数据出现重复消费的问题。
            List<String> list = new ArrayList<>();
            for(String word : words){
                list.add(word);
            }
            collector.emit(input, new Values(list));
            //////////     正确示例     ////////////
             **/

            //使用 ack，确认消息处理成功
            collector.ack(tuple);

        }catch (Exception e){
            e.printStackTrace();

            //如果失败了需要重试，必须使用fail函数进行标记，之后spout就会进行重发
            collector.fail(tuple);
        }
    }

    /**
     * 用于声明当前Bolt发送的Tuple中包含的字段(field)，和Spout中类似
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    /**
     * 同ISpout的close方法，在关闭前调用。同样不保证其一定执行。
     */
    @Override
    public void cleanup() {

    }
}
