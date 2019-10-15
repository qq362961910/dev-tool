package cn.t.tool.nettytool.decoder;

import cn.t.tool.nettytool.analyser.ByteBufAnalyser;
import cn.t.tool.nettytool.util.NullMessage;
import cn.t.util.common.digital.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class NettyTcpDecoder extends ByteToMessageDecoder {

    private static Logger logger = LoggerFactory.getLogger(NettyTcpDecoder.class);

    private ByteBufAnalyser byteBufAnalyser;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if(in.isReadable()) {
            int readerIndex = in.readerIndex();
            Object msg = byteBufAnalyser.analyse(ctx, in);
            if(msg == null) {
                logger.info("消息不完整，重置读取索引");
                in.readerIndex(readerIndex);
            } else {
                if(NullMessage.getNullMessage() != msg) {
                    logger.info("读取到消息，类型为: {}", msg.getClass().getName());
                    out.add(msg);
                } else {
                    logger.info("未读取到消息，不重置读取索引");
                }
            }
        }
    }

    public NettyTcpDecoder(ByteBufAnalyser byteBufAnalyser) {
        this.byteBufAnalyser = byteBufAnalyser;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("连接建立成功: {}", ctx.channel().remoteAddress());
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("连接断开: {}", ctx.channel().remoteAddress());
        ctx.fireChannelInactive();
    }
}
