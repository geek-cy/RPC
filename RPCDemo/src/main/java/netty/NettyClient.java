package netty;

import client.RpcClient;
import codec.CommonDecoder;
import codec.CommonEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import entity.RpcRequest;
import entity.RpcResponse;
import serializer.KryoSerializer;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/25 0:10
 */
@Slf4j
@AllArgsConstructor
public class NettyClient implements RpcClient {

    private final String host;
    private final int port;
    private static final Bootstrap bootstrap;

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CommonDecoder())
                                .addLast(new CommonEncoder(new KryoSerializer()))
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try{
            ChannelFuture future = bootstrap.connect(host, port).sync();
            log.info("客户端连接到服务器{}:{}",host,port);
            Channel channel = future.channel();
            if(channel != null){
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if (future1.isSuccess()){
                        log.info(String.format("客户端发送消息:%s",rpcRequest.toString()));
                    } else {
                        log.error("发送消息时出错",future1.cause());
                    }
                });
                channel.closeFuture().sync();
                // 通过AttributeKey阻塞获得返回结果
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse response = channel.attr(key).get();
            }
        } catch (InterruptedException e) {
            log.error("发送消息时发生错误",e);
        }
        return null;
    }
}
