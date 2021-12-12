package com.demo.concurrent.collection;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/4/2 下午10:14
 * @Description:
 */

public class PriorityQueueExample implements Comparable<PriorityQueueExample>{

    private int id ;
    private String name;

    public PriorityQueueExample(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(PriorityQueueExample o) {
        return this.getId()>o.getId() ? 1 : (this.getId()<o.getId() ? -1 : 0);
    }


    public static void main(String[] args) throws InterruptedException {

        PriorityQueueExample p1 = new PriorityQueueExample(1, "aaa");
        PriorityQueueExample p2 = new PriorityQueueExample(5, "bbb");
        PriorityQueueExample p3 = new PriorityQueueExample(3, "ccc");
        PriorityQueueExample p4 = new PriorityQueueExample(9, "ddd");

        PriorityBlockingQueue<PriorityQueueExample> queue = new PriorityBlockingQueue<PriorityQueueExample>();
        queue.offer(p1);
        queue.offer(p2);
        queue.offer(p3);
        queue.offer(p4);
        //添加完成之后，是不会按照大小重新排列的
        //只有再取数据的时候才会重新排列，返回最小的值
        System.out.println(queue.take().getId());
        System.out.println(queue.take().getId());
        System.out.println(queue.take().getId());
        System.out.println(queue.take().getId());
    }
}
