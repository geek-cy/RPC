package client.netty;

import client.RpcClient;
import codec.CommonDecoder;
import codec.CommonEncoder;
import entity.RpcRequest;
import entity.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import serializer.KryoSerializer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Cy
 * @date 2021/6/6 0:57
 */
@Slf4j
public class NettyClient implements RpcClient {

    private static Bootstrap bootstrap;

    static {
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //是否开启 TCP 底层心跳机制,当连接处于空闲状态会给服务器发送一个数据包
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(0,5,0, TimeUnit.SECONDS));
                        pipeline.addLast(new CommonEncoder(new KryoSerializer()));
                        pipeline.addLast(new CommonDecoder());
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try {
//            CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
            ChannelFuture channelFuture = bootstrap.connect("192.168.31.92", 8000).sync();
            log.info("Netty客户端成功连接到服务器");
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(rpcRequest).addListener(future -> {
                if(future.isSuccess()) log.info("Netty客户端向服务端发送请求:{}",rpcRequest.toString());
                else log.error("Netty客户端向服务端发送请求错误");
            });
            channel.closeFuture().sync();
            AttributeKey<Object> key = AttributeKey.valueOf("RpcResponse");
            RpcResponse response = (RpcResponse)channel.attr(key).get();
            return response.getData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
