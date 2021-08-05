package service.netty;

import entity.RpcRequest;
import entity.RpcResponse;
import handler.RequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 入站处理（Simple无需关心资源释放）
 * @author Cy
 * @date 2021/6/6 0:00
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final RequestHandler requestHandler;

    public NettyServerHandler() {
        this.requestHandler = new RequestHandler();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("Netty客户端优雅关闭");
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        if(msg.getHeartBeat()){
            log.info("接受到客户端心跳包");
            return;
        }
        log.info("服务器接收到请求:{}",msg);
        Object result = requestHandler.handler(msg);
        if(ctx.channel().isActive() && ctx.channel().isWritable()){
            ctx.writeAndFlush(RpcResponse.success(result,msg.getRequestId()));
        } else {
            log.error("通道不可写");
        }
    }

    /**
     * 当客户端连接异常时会触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("入站处理器有错误发生");
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 若没检测到心跳会触发此方法
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            if(state == IdleState.READER_IDLE){
                log.info("读空闲超时，关闭连接");
                ctx.close();
            }
        } else super.userEventTriggered(ctx, evt);
    }
}
