package com.nanomt88.demo.trident.strategy;

import com.nanomt88.demo.helloworld.WriterBolt;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 写入数据到文件中
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-05 9:24
 **/
public class WriteFunction extends BaseFunction {

    private static final Logger logger = LoggerFactory.getLogger(WriterBolt.class);

    private FileWriter writer;

    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        String text = tuple.getStringByField("sub");
        try {
            if(writer == null){
                if(System.getProperty("os.name").equals("Windows 10")) {
                    writer = new FileWriter("D:\\temp\\"+this);
                } else if(System.getProperty("os.name").equals("Linux")){
                    System.out.println("----:" + System.getProperty("os.name"));
                    writer = new FileWriter("/root/temp/" + this);
                }
            }

            logger.info("[write] : 写入文件：{}", text);

            writer.write(text);
            writer.write("\n");
            writer.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
