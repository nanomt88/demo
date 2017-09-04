package com.nanomt88.risk.trident.count;

import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;

/**
 * 切分单词的 function
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-05 16:01
 **/
public class SplitFunction extends BaseFunction {
    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        String subjects = tuple.getStringByField("subjects");
        //获取tuple输入内容
        //逻辑处理，然后发射给下一个组件
        for(String sub : subjects.split(" ")) {
            collector.emit(new Values(sub));
        }
    }
}
