package cn.t.tool.netproxytool.http.server.promise;

import cn.t.tool.netproxytool.common.promise.MessageSender;
import cn.t.tool.netproxytool.http.server.handler.ForwardingMessageHandler;
import cn.t.tool.netproxytool.http.server.handler.HttpRequestHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

/**
 * http代理结果监听器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-27 15:42
 **/
@Slf4j
public class HttpProxyForwardingResultListener implements ChannelFutureListener {

    private ChannelHandlerContext channelHandlerContext;
    private MessageSender messageSender;
    private String targetHost;
    private int targetPort;

    @Override
    public void operationComplete(ChannelFuture future) {
        if(future.isSuccess()) {
            log.info("[{}]: 发送代理结果成功, 目的地址: [{}:{}]", channelHandlerContext.channel().remoteAddress(), targetHost, targetPort);
            //已经通知客户端代理成功, 切换handler
            ChannelPipeline channelPipeline = channelHandlerContext.channel().pipeline();
            channelPipeline.remove(HttpServerCodec.class);
            channelPipeline.remove(HttpObjectAggregator.class);
            channelPipeline.remove(HttpRequestHandler.class);
            channelPipeline.addLast("proxy-fording-handler", new ForwardingMessageHandler(messageSender));
        } else {
            log.error("[{}]: 发送代理结果失败, 目的地址: [{}:{}], 原因: {}", channelHandlerContext.channel().remoteAddress(), targetHost, targetPort, future.cause());
            channelHandlerContext.close();
        }
    }

    public HttpProxyForwardingResultListener(ChannelHandlerContext channelHandlerContext, MessageSender messageSender, String targetHost, int targetPort) {
        this.channelHandlerContext = channelHandlerContext;
        this.messageSender = messageSender;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
    }
}