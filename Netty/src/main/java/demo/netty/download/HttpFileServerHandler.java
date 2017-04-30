package demo.netty.download;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/29 下午9:59
 * @Description:  HTTP 文件下载 处理类
 *
 */

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final String CONTENT = "hello world, this is a http demo";

    private String url = null;

    public HttpFileServerHandler(String url){
        this.url = url;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        //对请求的编码结果进行判断，不正确则返回 BAD_REQUEST
        if(! request.decoderResult().isSuccess()){
            //400
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        //对请求方式进行判断，如果不是get 方法返回错误：不接受除GET以外的方法
        if(request.method() != HttpMethod.GET){
            //405
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        //获取请求URI路径
        final String uri = request.uri();
        //分析URI路径，转换为本地文件系统路径
        final String path = sanitizeURL(uri);
        //如果请求地址不合法，返回错误
        if(path == null){
            //403
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }
        //创建File对象，读取本地文件
        File file = new File(path);
        //不存在或者隐藏 返回错误 404
        if(file.isHidden() || !file.exists()){
            //404
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        //如果为文件夹，展示文件夹内容
        if(file.isDirectory()){
            if(uri.endsWith("/")){
                //如果以 "/"结束，说明是访问的一个文件目录：则展开文件目录内的内容
                sendFileList(ctx, file);
            }else {
                //如果不是"/"结束，则重定向，补全"/"后再次请求。
                sendRedirect(ctx, uri+"/");
            }
            return;
        }

        //如果创建的file对象不是文件类型，返回错误
        if(!file.isFile()){
            //403
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }

        //随机文件读写类
        RandomAccessFile randomAccessFile = null;
        try {
            //以只读方式打开文件
            randomAccessFile = new RandomAccessFile(file, "r");
        }catch (FileNotFoundException e){
            //404
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        //文件长度
        long length = randomAccessFile.length();
        //建立响应对象
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        //设置响应头
        HttpUtil.setContentLength(response, length);
        //设置响应头
        setContentTypeHeader(response, file);
        //如果一直保存连接，则设置响应头为keep_alive
        if(HttpUtil.isKeepAlive(request)){
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        //进行文件读写
        ctx.write(response);

        //构造发送文件线程，将文件写入到Chunked缓冲区中
        ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile,
                0, length, 1024 * 8), ctx.newProgressivePromise());
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                if(total < 0){
                    System.out.println("传输进度 ： " + progress);
                }else{
                    System.out.println("传输进度 ： " + progress+" / "+total +" ( "+progress/total+" %)");
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                System.out.println("传输完成！！！");
            }
        });

        //在使用Chunked编码的时候，最后要发送一个编码结束的空消息进行标记，表示所有消息体已经发送完成
        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        //如果当前链接请求非Keep-Alive， 最后一个包发送完成后，服务器需要主动断开连接
        if(!HttpUtil.isKeepAlive(request)){
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void setContentTypeHeader(HttpResponse response, File file) {
        //使用mime对象获取文件类型
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, fileTypeMap.getContentType(file.getPath()));
    }

    private void sendRedirect(ChannelHandlerContext ctx, String uri) {
        //建立响应对象
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        //设置新的请求地址放入响应对象中去， 让浏览器进行重定向
        response.headers().set(HttpHeaderNames.LOCATION, uri);
        //使用ctx对象写出并且刷新到SocketChannel中去 并主动关闭连接(这里是指关闭处理发送数据的线程连接)
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    //文件是否被允许访问下载验证
    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    /**
     * 遍历 文件夹下面的文件，返回给页面
     * @param ctx
     * @param dir
     */
    private void sendFileList(ChannelHandlerContext ctx, File dir) {
        //构造响应对象
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        //设置响应头
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

        // 追加文本内容
        StringBuilder ret = new StringBuilder();
        String dirPath = dir.getPath();
        ret.append("<!DOCTYPE html>\r\n");
        ret.append("<html><head><title>");
        ret.append(dirPath);
        ret.append(" 目录：");
        ret.append("</title></head><body>\r\n");
        ret.append("<h3>");
        ret.append(dirPath).append(" 目录：");
        ret.append("</h3>\r\n");
        ret.append("<ul>");
        ret.append("<li>链接：<a href=\"../\">..</a></li>\r\n");

        // 遍历文件 添加超链接
        for (File f : dir.listFiles()) {
            //step 1: 跳过隐藏或不可读文件
            if (f.isHidden() || !f.canRead()) {
                continue;
            }
            String name = f.getName();
            //step 2: 如果不被允许，则跳过此文件
            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }
            //拼接超链接即可
            ret.append("<li>链接：<a href=\"");
            ret.append(name);
            ret.append("\">");
            ret.append(name);
            ret.append("</a></li>\r\n");
        }
        ret.append("</ul></body></html>\r\n");

        //构造数据，写入缓冲区
        ByteBuf byteBuf = Unpooled.copiedBuffer(ret, CharsetUtil.UTF_8);
        //进行写出操作
        response.content().writeBytes(byteBuf);
        //释放缓冲区
        byteBuf.release();
        //将ctx 对象写出并且刷新到SocketChannel中，并主动关闭连接（这里是指关闭处理发送数据的线程连接）
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    //非法URI正则
    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    /**
     * 解析URI，转换成本地文件路径
     * @param uri
     * @return
     */
    private String sanitizeURL(String uri) {
        try {
            URLDecoder.decode(uri, "UTF-8");   //UTF-8编码集
        } catch (UnsupportedEncodingException e) {
            try {
                URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                //抛出预想外异常信息
                throw  new Error("URL编码错误");
            }
        }

        // 对uri进行细粒度判断：4步验证操作
        // step 1 基础验证
        if (!uri.startsWith(url)) {
            return null;
        }
        // step 2 基础验证
        if (!uri.startsWith("/")) {
            return null;
        }
        // step 3 将文件分隔符替换为本地操作系统的文件路径分隔符
        uri = uri.replace('/', File.separatorChar);
        // step 4 二次验证合法性
        if (uri.contains(File.separator + '.')
                || uri.contains('.' + File.separator) || uri.startsWith(".")
                || uri.endsWith(".") || INSECURE_URI.matcher(uri).matches()) {
            return null;
        }
        //当前工程所在目录 + URI构造绝对路径进行返回
        return System.getProperty("user.dir") + File.separator + "Netty" + uri;
    }

    /**
     * 返回错误信息
     * @param ctx
     * @param status
     */
    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        //建立响应对象
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure: " + status.toString()+ "\r\n", CharsetUtil.UTF_8));
        //设置响应头信息
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        //使用ctx对象写出并且刷新到SocketChannel中去 并主动关闭连接(这里是指关闭处理发送数据的线程连接)
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if( ctx.channel().isActive()){
            sendError( ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            ctx.close();
        }
    }
}
