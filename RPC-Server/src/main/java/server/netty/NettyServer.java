package server.netty;

import codec.CommonDecoder;
import codec.CommonEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import provider.ServiceProviderImpl;
import serializer.CommonSerializer;
import server.AbstractRpcServer;

import java.util.concurrent.TimeUnit;

/**
 * @author Cy
 * @date 2021/6/4 22:16
 */
@Slf4j
public class NettyServer extends AbstractRpcServer {

    private final CommonSerializer commonSerializer;

    public NettyServer(String host,int port,int serializeCode){
        this.host = host;
        this.port = port;
        commonSerializer = CommonSerializer.getSerializer(serializeCode);
        serviceProvider = new ServiceProviderImpl();
    }

    @Override
    public void start() {
//        ShutdownHook.getShutdownHook().addClearAllHook();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,256)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.TCP_NODELAY,true)// 禁用算法，发送小包
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(30,0,0, TimeUnit.SECONDS))
                                    .addLast(new CommonDecoder())
                                    .addLast(new CommonEncoder(commonSerializer))
                                    .addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("启动Netty服务器时发生错误",e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
