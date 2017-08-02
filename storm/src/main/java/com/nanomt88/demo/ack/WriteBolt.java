package com.nanomt88.demo.ack;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 将前面切分的单词进行输出
 *
 *      测试数据处理失败之后，进行重发的示例
 *
 *      注意： 这里消费重试的时候，可能会出现消息重复消费的问题。针对重复消费的问题，建议在消费端做幂等性判断；
 *          或者不要将一个 数据拆分成后发送
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-02 8:15
 **/
public class WriteBolt implements IRichBolt{

    private OutputCollector collector;

    private boolean flag = true;

    private FileWriter writer;

    /**
     *  此方法和Spout中的open方法类似，在集群中一个worker中的task初始化时调用。 它提供了bolt执行的环境
     * @param stormConf
     * @param context
     * @param collector
     */
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        try {
            writer = new FileWriter("d://message.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
//        List<String> list = (List<String>) tuple.getValueByField("word");
//        System.out.println("============================>"+list);

        String word = tuple.getString(0);

        try {

            if(flag && word.equals("c++")){
                flag = false;
                int a = 1 / 0;
            }

            writer.write(word);
            writer.write("\r\n");
            writer.flush();

            //使用emit发射的时候，必须带tuple；这样spout 的 ack函数才能执行
            collector.emit(tuple, new Values(word));
            collector.ack(tuple);

        }catch (Exception e){
            e.printStackTrace();
            collector.fail(tuple);
        }
    }

    /**
     * 用于声明当前Bolt发送的Tuple中包含的字段(field)，和Spout中类似
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
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
