package cn.t.tool.netproxytool.socks5.server.handler;

import cn.t.tool.netproxytool.exception.ProxyException;
import cn.t.tool.netproxytool.socks5.model.CmdRequest;
import cn.t.tool.netproxytool.socks5.model.ConnectionLifeCycle;
import cn.t.tool.netproxytool.socks5.model.ConnectionLifeCycledMessage;
import cn.t.tool.netproxytool.socks5.model.NegotiateRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author yj
 * @since 2020-01-12 16:28
 **/
public class Socks5MessageHandler extends SimpleChannelInboundHandler<ConnectionLifeCycledMessage> {

    private final NegotiateRequestHandler negotiateRequestHandler = new NegotiateRequestHandler();
    private final AuthenticationRequestHandler authenticationRequestHandler = new AuthenticationRequestHandler();
    private final CmdRequestHandler cmdRequestHandler = new CmdRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ConnectionLifeCycledMessage lifeCycledMessage) {
        ConnectionLifeCycle lifeCycle = lifeCycledMessage.getLifeCycle();
        Object message;
        switch (lifeCycle.getCurrentSocks5Step()) {
            case NEGOTIATE: message = negotiateRequestHandler.handle((NegotiateRequest)lifeCycledMessage.getMessage(), lifeCycle); break;
            case AUTHENTICATION: message = authenticationRequestHandler.handle(lifeCycledMessage.getMessage(), lifeCycle); break;
            case COMMAND_EXECUTION: {
                SocketAddress socketAddress = channelHandlerContext.channel().remoteAddress();
                if(socketAddress instanceof InetSocketAddress) {
                    message = cmdRequestHandler.handle((CmdRequest)lifeCycledMessage.getMessage(), channelHandlerContext);
                } else {
                    throw new ProxyException(String.format("不支持的socket地址类型: %s", socketAddress.getClass().getName()));
                }
                break;
            }
            default: throw new ProxyException(String.format("未处理的解析步骤: %s", lifeCycle.getCurrentSocks5Step()));
        }
        if(message != null) {
            channelHandlerContext.writeAndFlush(message);
        }
    }
}
