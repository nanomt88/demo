package com.nanomt88.demo.helloworld;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 写单词的bolt
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-31 6:57
 **/
public class WriterBolt extends BaseBasicBolt {

    private static final long serialVersionUID = -8370717450482910947L;

    private static final Logger LOGGER = LoggerFactory.getLogger(WriterBolt.class);

    private FileWriter writer ;

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //获取上一个组件所声明的Field
        String text = input.getStringByField("write");

        try {
            if(writer == null){
                if(System.getProperty("os.name").equals("Windows 10")) {
                    writer = new FileWriter("D:\\temp\\"+this);
                } else if(System.getProperty("os.name").equals("Linux")){
                    System.out.println("----:" + System.getProperty("os.name"));
                    writer = new FileWriter("/root/temp/" + this);
                }
            }

            LOGGER.info("[write] : 写入文件：{}", text);
            writer.write(text);
            writer.write("\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
