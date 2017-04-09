package com.demo.concurrent.collection;

import java.util.Iterator;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/6 下午9:58
 * @Description:
 */

public class DelayQueueExample {
    public static void main(String[] args) throws InterruptedException {
        DelayQueue<MyTask>  queue = new DelayQueue();
        MyTask t1 = new MyTask(3,"aaa", 3);
        MyTask t2 = new MyTask(1, "bbb", 5);
        MyTask t3 = new MyTask(4, "ccc", 2);

        queue.add(t1);
        queue.add(t2);
        queue.add(t3);

        for(Iterator<MyTask> iterator = queue.iterator(); iterator.hasNext();){
            System.out.println(iterator.next());
        }

        while (true){
            MyTask take = queue.take();
            System.out.println("元素失效：" + take);
        }
    }

    private static class MyTask implements Delayed,Runnable {
        private int id;
        private String name;
        private long time ;
        public MyTask(int id, String name, int seconds){
            this.id = id;
            this.name = name;
            this.time = seconds;
        }

        public int getId() {
            return id;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert( time * 1000L - System.currentTimeMillis(), TimeUnit.MILLISECONDS );
        }

        @Override
        public int compareTo(Delayed o) {
            if(o==null || !(o instanceof MyTask) ){
                return 1;
            }
            if(this == o)    return 0;

            MyTask task = (MyTask) o;

            return this.getId()>task.getId() ? 1 : this.getId()<task.getId() ? -1 : 0 ;
        }

        @Override
        public String toString() {
            return "MyTask{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public void run() {
            System.out.println("我到期失效了。。。。"+this);
        }
    }
}
