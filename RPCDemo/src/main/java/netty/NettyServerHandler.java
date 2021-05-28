package netty;

import registry.ServiceProviderImpl;
import socket.RequestHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import registry.ServiceProvider;
import entity.RpcRequest;
import entity.RpcResponse;

/**
 * @Description 位于责任链尾部
 * @Author Cy
 * @Date 2021/5/25 16:18
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static RequestHandler requestHandler;
    private static ServiceProvider serviceProvider;

    static {
        requestHandler = new RequestHandler();
        serviceProvider = new ServiceProviderImpl();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            log.info("服务器收到请求:{}",msg);
            String interfaceName = msg.getInterfaceName();
            Object service = serviceProvider.getService(interfaceName);
            Object handler = requestHandler.handler(msg, service);
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(handler));
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:{}",cause.getMessage());
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
