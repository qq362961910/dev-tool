package cn.t.tool.netproxytool.common.promise;

/**
 * 连接结果通知
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-22 21:48
 **/
@FunctionalInterface
public interface ProxyBuildResultListener {
    void handle (byte status, MessageSender sender);
}
