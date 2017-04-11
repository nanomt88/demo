package com.demo.product;

import com.demo.generate.Order;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by ZBOOK-17 on 2017/4/11.
 */
public class Main {

    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();
        int bufferSize = 1024;

        ExecutorService executorService = Executors.newFixedThreadPool(8);

        EventFactory<Order> eventFactory = new EventFactory<Order>() {
            @Override
            public Order newInstance() {
                return new Order();
            }
        };
        // BusySpinWaitStrategy ： 自旋等待，类似Linux Kernel使用的自旋锁。低延迟但同时对CPU资源的占用也多。
//        Disruptor<Order> disruptor = new Disruptor<Order>(eventFactory , bufferSize, executorService,
//                    ProducerType.SINGLE, new BusySpinWaitStrategy());

        Disruptor<Order> disruptor = new Disruptor<Order>(eventFactory, bufferSize, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }, ProducerType.SINGLE, new BusySpinWaitStrategy());


    }
}
