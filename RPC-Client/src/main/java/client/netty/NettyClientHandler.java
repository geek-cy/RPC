package client.netty;

import entity.RpcRequest;
import entity.RpcResponse;
import factory.SingletonFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author Cy
 * @date 2021/6/6 15:13
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse<Object>> {

    private final UnprocessedRequest unprocessedRequest;
    private final NettyClient nettyClient;
    public NettyClientHandler() {
        unprocessedRequest = SingletonFactory.getInstance(UnprocessedRequest.class);
        nettyClient = SingletonFactory.getInstance(NettyClient.class);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive");
        super.channelInactive(ctx);
    }

    // Remove all handlers sequentially if channel is closed and unregister
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelUnregistered");
        ctx.close();
        /*log.info("断线重连");
        nettyClient.connect((InetSocketAddress) ctx.channel().remoteAddress());*/
    }

    /**
     * 调用unprocessedRequest赋值
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        /*log.info("绑定AttributeKey");
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("RpcResponse");
        ctx.channel().attr(key).set(msg);
        ctx.channel().close();*/
        log.info("Netty客户端接收到消息:{}",msg);
        unprocessedRequest.complete(msg);
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("客户端入站处理器出现错误",cause);
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            if(state == IdleState.WRITER_IDLE){
                Channel channel = nettyClient.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                RpcRequest rpcRequest = new RpcRequest();
                rpcRequest.setHeartBeat(true);
                log.info("发送心跳包[{}]",ctx.channel().remoteAddress());
                //  Send the heartbeat and close the connection if the send operation fails
                channel.writeAndFlush(rpcRequest).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            // Not of type IdleStateEvent pass it to the next handler in the ChannelPipeline
            super.userEventTriggered(ctx,evt);
        }
    }
}
