package cn.t.tool.nettytool.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 事件日志打印处理器
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-25 10:08
 **/
public class EventLoggingHandler extends LoggingHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (logger.isEnabled(internalLevel)) {
            if(msg instanceof ByteBuf) {
                logger.log(internalLevel, formatByteBuf(ctx, "READ", (ByteBuf)msg));
                ctx.fireChannelRead(msg);
            } else {
                super.channelRead(ctx, msg);
            }
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (logger.isEnabled(internalLevel)) {
            if(msg instanceof ByteBuf) {
                logger.log(internalLevel, formatByteBuf(ctx, "WRITE", (ByteBuf)msg));
                ctx.write(msg, promise);
            } else {
                super.write(ctx, msg, promise);
            }
        }

    }

    private static String formatByteBuf(ChannelHandlerContext ctx, String eventName, ByteBuf msg) {
        String chStr = ctx.channel().toString();
        int length = msg.readableBytes();
        if (length == 0) {
            return chStr + ' ' + eventName + ": 0B";
        } else {
            return chStr + ' ' + eventName + ": " + length + 'B';
        }
    }

    public EventLoggingHandler(LogLevel level) {
        super(level);
    }
}