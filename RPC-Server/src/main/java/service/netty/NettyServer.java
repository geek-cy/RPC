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
                    .childOption(ChannelOption.TCP_NODELAY, true)// 禁用算法，发送小包
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
            log.error("启动Netty服务器时发生错误", e);
        } finally {
            log.info("关闭");
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
            // 1、先判断main有无ServiceScan注解
            if (!clazz.isAnnotationPresent(ServiceScan.class)) {
                log.error("启动类缺少@ServiceScan注解");
                throw new IllegalArgumentException();
            }
            // 2、获取包名
            String basePackage = clazz.getAnnotation(ServiceScan.class).value();
            // 3、若ServiceScan注解上未写值则默认扫启动类所在的包
            if ("".equals(basePackage)) {
                basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
            }
            Set<Class<?>> classes = ReflectUtil.getClasses(basePackage);
            // 4、遍历找有Service注解的类
            for (Class<?> c : classes) {
                if (c.isAnnotationPresent(Service.class)) {
                    String serviceName = c.getAnnotation(Service.class).value();
                    Object obj;
                    try {
                        // 5、反射创建对象
                        obj = c.newInstance();
                    } catch (IllegalAccessException | InstantiationException e) {
                        log.error("创建" + c + "时有错误发生");
                        continue;
                    }
                    // 6、注册服务名字，若为空则默认注册实现的接口
                    if ("".equals(serviceName)) {
                        Class<?>[] interfaces = c.getInterfaces();
                        for (Class<?> i : interfaces) {
                            publishService(obj, i.getCanonicalName());
                        }
                        // 7、注册Service注解上的服务名字
                    } else publishService(obj, serviceName);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
