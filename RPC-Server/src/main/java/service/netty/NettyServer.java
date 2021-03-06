package service.netty;

import annotation.Service;
import annotation.ServiceScan;
import codec.CommonDecoder;
import codec.CommonEncoder;
import hook.ShutdownHook;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import provider.ServiceProvider;
import provider.ServiceProviderImpl;
import register.NacosServiceRegistry;
import register.ServiceRegistry;
import serializer.CommonSerializer;
import service.RpcServer;
import utils.ReflectUtil;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Cy
 * @date 2021/6/4 22:16
 */
@Slf4j
public class NettyServer implements RpcServer {

    protected String host;
    protected int port;
    protected ServiceProvider serviceProvider;
    private final CommonSerializer commonSerializer;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workGroup;
    private final ServiceRegistry serviceRegistry;

    public NettyServer(String host, int port, int serializeCode) {
        this.host = host;
        this.port = port;
        commonSerializer = CommonSerializer.getSerializer(serializeCode);
        serviceProvider = new ServiceProviderImpl();
        serviceRegistry = new NacosServiceRegistry();
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();
        scanService();
    }

    @Override
    public <T> void publishService(T service, String serviceName) {
        serviceProvider.addService(service, serviceName);
        serviceRegistry.registry(serviceName, new InetSocketAddress(host, port));
    }

    @Override
    public void start() {
        try {
            ShutdownHook.shutdownHook();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)// ???????????????????????????
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS))
                                    .addLast(new CommonDecoder())
                                    .addLast(new CommonEncoder(commonSerializer))
                                    .addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            // Wait until Channel closes. This will block.
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("??????Netty????????????????????????", e);
        } finally {
            log.info("??????");
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public void close() {
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    @Override
    public void scanService() {
        String mainClassName = ReflectUtil.getStackBottom();
        try {
            Class<?> clazz = Class.forName(mainClassName);
            // 1????????????main??????ServiceScan??????
            if (!clazz.isAnnotationPresent(ServiceScan.class)) {
                log.error("???????????????@ServiceScan??????");
                throw new IllegalArgumentException();
            }
            // 2???????????????
            String basePackage = clazz.getAnnotation(ServiceScan.class).value();
            // 3??????ServiceScan???????????????????????????????????????????????????
            if ("".equals(basePackage)) {
                basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
            }
            Set<Class<?>> classes = ReflectUtil.getClasses(basePackage);
            // 4???????????????Service????????????
            for (Class<?> c : classes) {
                if (c.isAnnotationPresent(Service.class)) {
                    String serviceName = c.getAnnotation(Service.class).value();
                    Object obj;
                    try {
                        // 5?????????????????????
                        obj = c.newInstance();
                    } catch (IllegalAccessException | InstantiationException e) {
                        log.error("??????" + c + "??????????????????");
                        continue;
                    }
                    // 6???????????????????????????????????????????????????????????????
                    if ("".equals(serviceName)) {
                        Class<?>[] interfaces = c.getInterfaces();
                        for (Class<?> i : interfaces) {
                            publishService(obj, i.getCanonicalName());
                        }
                        // 7?????????Service????????????????????????
                    } else publishService(obj, serviceName);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
