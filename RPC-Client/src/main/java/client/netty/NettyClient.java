package client.netty;

import client.RpcClient;
import codec.CommonDecoder;
import codec.CommonEncoder;
import entity.RpcRequest;
import entity.RpcResponse;
import factory.SingletonFactory;
import hook.ShutdownHook;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import register.NacosServiceDiscovery;
import register.ServiceDiscovery;
import serializer.KryoSerializer;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Cy
 * @date 2021/6/6 0:57
 */
@Slf4j
public class NettyClient implements RpcClient {

    private final Bootstrap bootstrap;
    private final EventLoopGroup group;
    private final ChannelProvider channelProvider;
    private final UnprocessedRequest unprocessedRequest;
    private final ServiceDiscovery serviceDiscovery;

    public NettyClient() {
        ShutdownHook.shutdownHook();
        group = new NioEventLoopGroup();
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
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
        this.unprocessedRequest = SingletonFactory.getInstance(UnprocessedRequest.class);
        this.serviceDiscovery = new NacosServiceDiscovery();
    }

    @SneakyThrows
    public Channel connect(InetSocketAddress inetSocketAddress){
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future ->{
            if(future.isSuccess()){
                log.info("Netty客户端成功连接");
                // 子线程去获取channel
                completableFuture.complete(future.channel());
            } else {
                log.error("Netty客户端连接失败");
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    @SneakyThrows
    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        // build return value
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        // get server address
//        InetSocketAddress inetSocketAddress = new InetSocketAddress("192.168.31.92", 8000);
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        // get  server address related channel
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            log.info("channel处于连接状态");
            unprocessedRequest.put(rpcRequest.getRequestId(),resultFuture);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener)future -> {
                if (future.isSuccess()) log.info("Netty客户端向服务端发送请求:{}", rpcRequest.toString());
                else {
                    log.error("Netty客户端向服务端发送请求失败",future.cause());
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                }
            });
        } else {
            log.error("channel连接失败");
            throw new IllegalStateException();
        }
        // 为了复用此处就不能阻塞关闭,引入CompletableFuture
        /*channel.closeFuture().sync();
        log.info("channel关闭");
        AttributeKey<Object> key = AttributeKey.valueOf("RpcResponse");
        RpcResponse response = (RpcResponse) channel.attr(key).get();
        return response.getData();*/
        return resultFuture;
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress){
        Channel channel = channelProvider.get(inetSocketAddress);
        if(channel == null){
            log.info("新的channel");
            channel = connect(inetSocketAddress);
            channelProvider.set(inetSocketAddress,channel);
        }
        return channel;
    }

    @Override
    public void close(){
        log.info("group关闭");
        group.shutdownGracefully();
    }

}
