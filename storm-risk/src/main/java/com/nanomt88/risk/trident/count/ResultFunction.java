package com.nanomt88.risk.trident.count;

import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;

/**
 * 打印计数结果的 function
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-05 16:02
 **/
public class ResultFunction extends BaseFunction {
    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        System.out.println("--------------------------------");
        //获取tuple输入内容
        String sub = tuple.getStringByField("sub");
        Long count = tuple.getLongByField("count");
        System.out.println(sub +" : "+ count);
        System.out.println();
    }
}
