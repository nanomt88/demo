package com.demo.helloworld;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/9 上午8:54
 * @Description:
 *              很明显的是：当用一个简单队列来发布事件的时候会牵涉更多的细节，这是因为事件对象还需要预先创建。
 * 发布事件最少需要两步：获取下一个事件槽并发布事件（发布事件的时候要使用try/finnally保证事件一定会被发布）。
 * 如果我们使用RingBuffer.next()获取一个事件槽，那么一定要发布对应的事件。
 * 如果不能发布事件，那么就会引起Disruptor状态的混乱。
 * 尤其是在多个事件生产者的情况下会导致事件消费者失速，从而不得不重启应用才能会恢复。
 */

public class LongEventProducer {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer buffer){
        ringBuffer = buffer;
    }

    /**
     * publish用来发布事件，每调用一次就发布一次事件
     * @param byteBuffer    用于传递时间给消费者
     */
    public void publish(ByteBuffer byteBuffer){
        //1. 可以把ringBuffer看做一个环形的事件槽，那么next()方法就是取出下一个事件槽的位置
        long sequence = ringBuffer.next();
        try{
            //2. 用上面的索引取出一个空的事件用于填充 （获取该序号对应的事件对象）
            LongEvent longEvent = ringBuffer.get(sequence);
            //3. 对要传递的对象设置业务数据
            longEvent.setValue(byteBuffer.getLong(0));
            //System.out.println(Thread.currentThread().getName()+" value:"+longEvent.getValue());
        }finally {
            //4. 发布事件
            // 注意：最后的ringBuffer.publish()必须放在finally中，确保肯定会被调用。
            //如果某个请求的sequence没有发布，就会堵塞后续的发布操作或者其他的producer
            ringBuffer.publish(sequence);
        }

    }
}
