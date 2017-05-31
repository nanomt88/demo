package com.demo.helloworld;

import com.lmax.disruptor.EventFactory;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/9 上午8:50
 * @Description:
 *             需要disruptor创建时间，需要实现EventFactory接口的newInstance接口
 *             来实例化我们的对象
 */

public class LongEventFactory implements EventFactory<LongEvent>{

    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
