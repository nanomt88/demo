package com.demo.multi;

import com.lmax.disruptor.RingBuffer;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/10 上午7:47
 * @Description:   生产者
 */

public class Producer {

    private RingBuffer<Order> ringBuffer = null;

    public Producer(RingBuffer<Order> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    /**
     * 生产者producer对象 通过onData方法发布事件，每调用一次就会发布一次事件
     * 参数id ： 会通过事件传递给消费者
     * @param id
     */
    public void onData(String id){
        //如果把ringBuffer看做一个环形队列，next()方法就是获取下一个事件槽
        long sequence = ringBuffer.next();
        try{
            // 用上面的索引在ringBuffer上面取得一个空的事件，用于填充（获取该序号对应的事件）
            Order order = ringBuffer.get(sequence);
            //设置要通过事件传递的业务数据
            order.setId(id);
        }finally {
            //在最后一定要记得发布事件
            //注意，最后的 ringBuffer.publish 方法必须包含在 finally 中以确保必须得到调用；
            // 如果某个请求的 sequence 未被提交，将会堵塞后续的发布操作或者其它的 producer
            ringBuffer.publish(sequence);
        }

    }
}
