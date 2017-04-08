package com.demo.concurrent.threadpool;

/**
 * @Copyright 		(c) Lictecs
 * @author 			xudong.hong@lictecs.com
 * @Date 			2012-11-3
 * @Description: 
 *					线程池使用简单样例
 */

public class SimpleSample {
    public static void main(String[] args){
    	//////////////////////////////////////////////
    	//1 初始化，创建线程池
        WorkerPool<Callable> pool = new WorkerPool<Callable>();
        System.out.println("Init OK.");
        //启动线程池
        pool.start();
        System.out.println("Begin to run...");
        //////////////////////////////////////////////
    	//2 运行过程
        //创建自定义任务添加任务到线程池中
        SimpleCallnable a = new SimpleCallnable("A");
        //对任务设置自定义监听器
        a.registListener(new SimpleCallableListener());
        
        SimpleCallnable b = new SimpleCallnable("B");
        //设置优先级为最低
        b.setPriority(SimpleCallnable.MIN_PRIORITY);
        
        SimpleCallnable c = new SimpleCallnable("C");
        //设置优先级为最高
        c.setPriority(SimpleCallnable.MAX_PRIORITY);
        
        SimpleCallnable d = new SimpleCallnable("D");
        SimpleCallnable e = new SimpleCallnable("E");
    
        SimpleCallnable f = new SimpleCallnable("F");
        f.setPriority(SimpleCallnable.MIN_PRIORITY);
        
        SimpleCallnable g = new SimpleCallnable("G");
        SimpleCallnable h = new SimpleCallnable("H");
        
        SimpleCallnable i = new SimpleCallnable("I");
        i.setPriority(SimpleCallnable.MAX_PRIORITY);
        
        SimpleCallnable j = new SimpleCallnable("J");
        
        pool.submit(a);
        pool.submit(b);
        pool.submit(c);
        pool.submit(d);
        pool.submit(e);
        //让线程睡眠一段时间，完成所有任务之后在添加新任务
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        ////继续添加任务
        pool.submit(f);
        pool.submit(g);
        pool.submit(h);
        pool.submit(i);
        pool.submit(j);
        
        /**
         * 直接实现 Callable 的任务创建方式
         */
        pool.submit(new Callable(){
            @Override
            public int call() throws Exception {
                System.out.println("My Callbale Class...");
                return 0;
            }
        });
        //让线程睡眠一段时间，完成所有任务
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        //////////////////////////////////////////////
    	//3 结束，停止所有线程
        pool.stop();
        System.out.println("Ended.");
    }
}

/**
 * 自定义任务，继承 AbstractCallable 的创建方式
 */
class SimpleCallnable extends AbstractCallable{
    private String name;
    
    public SimpleCallnable(String name){
        this.name = name;
    }
    
    @Override
    public int call() throws Exception {
        //线程睡眠，模拟业务处理时间
        Thread.currentThread();
        Thread.sleep(1000);
        System.out.println(name+" call, code here....");
        return 1;
    }
}

/**
 * 自定义任务执行过程中的例外或失败处理
 */
class SimpleCallableListener implements CallableListener {
    @Override
    public void errorHandle(Callable call,Exception ex) {
        System.out.println("Error Handle...");
    }
    
    @Override
    public void failedHandle(Callable call) {
        System.out.println("Failed Handle...");
    }
}
