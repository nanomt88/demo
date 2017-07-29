package demo.work.server;

import demo.work.ChargeQueryResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.*;

/**
 *  处理北京支付返回的查询结果，将结果放到线程池中异步发送
 *
 *      注意： 不要在 channelRead里面 同步执行业务代码，可能会因为业务代码慢拖垮服务器，切记！！！
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-29 17:18
 **/
public class BeijingPayServerHandler extends SimpleChannelInboundHandler<ChargeQueryResponse> {

    private ThreadPoolExecutor threadPool =  new ThreadPoolExecutor(10, 100,180L,
            TimeUnit.SECONDS, new LinkedBlockingQueue(10000), new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            //TODO 报警
            System.out.println("[ERROR_THREAD_POOL_FULL]BeijingPayServer thread pool: "+executor.toString()+" is full ," +
                    " task :" + r.toString());
            throw new RejectedExecutionException("Task " + r.toString() +
                    " rejected from " +
                    executor.toString());
        }
    });

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChargeQueryResponse msg) throws Exception{
        //提交到线程池
        //注意，此处一定要异步处理
        if(msg != null){
            threadPool.submit(new MessageSendTask(msg));
        }

    }
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.out.println("连接关闭 : " + ctx.channel().remoteAddress());
    }

}
