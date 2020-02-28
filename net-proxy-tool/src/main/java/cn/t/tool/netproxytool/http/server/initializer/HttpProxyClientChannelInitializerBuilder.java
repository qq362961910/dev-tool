package cn.t.tool.netproxytool.http.server.initializer;

import cn.t.tool.netproxytool.common.promise.MessageSender;
import cn.t.tool.netproxytool.common.promise.ProxyBuildResultListener;
import cn.t.tool.netproxytool.http.constants.HttpProxyClientConfig;
import cn.t.tool.netproxytool.http.server.handler.FetchMessageHandler;
import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;
import io.netty.handler.logging.LogLevel;

/**
 * @author yj
 * @since 2020-01-12 16:31
 **/
public class HttpProxyClientChannelInitializerBuilder extends NettyChannelInitializerBuilder {
    public HttpProxyClientChannelInitializerBuilder(MessageSender messageSender, ProxyBuildResultListener proxyBuildResultListener) {
        setLoggingHandlerLogLevel(LogLevel.DEBUG);
        setIdleState(HttpProxyClientConfig.HTTP_PROXY_READ_TIME_OUT_IN_SECONDS, HttpProxyClientConfig.HTTP_PROXY_WRITE_TIME_OUT_IN_SECONDS, HttpProxyClientConfig.HTTP_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        addChannelHandlerSupplier(() -> new FetchMessageHandler(messageSender, proxyBuildResultListener));
    }
}