package com.nanomt88.demo.util;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.nanomt88.demo.common.User;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * kyro 序列化实现类
 *      将所有需要添加序列化的类都添加到list集合中
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-24 8:25
 **/
public class SerializationOptionizerImpl implements SerializationOptimizer{
    @Override
    public Collection<Class> getSerializableClasses() {
        List<Class> classList = new LinkedList<Class>();

        //这里可以把所有需要进行序列化的类进行添加
        classList.add(User.class);

        return classList;
    }
}
