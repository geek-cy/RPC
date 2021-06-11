# RPC
参考自My-RPC-Framework和
本框架是一款基于 Netty 实现的 RPC 框架，客户端通过动态代理的方式调用远程计算机上的服务

## 特性

1、通过 Netty 进行网络传输并增加心跳机制保证客户端与服务端的连接

2、通过自定义通信协议解决 TCP 粘包问题，使用 Kryo 序列化和反序列化消息

3、保存本地服务信息，实现服务端启动与服务端的注册分离，使得服务端可以提供多个服务

4、使用 Nacos 作为注册中心，服务端向注册中心注册服务地址，客户端向注册中心请求服务地址

5、服务端实现了自动注册和自动注销功能

## 传输协议

调用参数与返回值的传输采用了如下协议以防止粘包：

```
+---------------+---------------+-----------------+-------------+
|  Magic Number |  Package Type | Serializer Type | Data Length |
|    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
+---------------+---------------+-----------------+-------------+
|                          Data Bytes                           |
|                   Length: ${Data Length}                      |
+---------------------------------------------------------------+
```

| 字段            | 解释                                                         |
| :-------------- | :----------------------------------------------------------- |
| Magic Number    | 魔数，表识一个 MRF 协议包，0xCAFEBABE                        |
| Package Type    | 包类型，标明这是一个调用请求还是调用响应                     |
| Serializer Type | 序列化器类型，标明这个包的数据的序列化方式                   |
| Data Length     | 数据字节的长度                                               |
| Data Bytes      | 传输的对象，通常是一个`RpcRequest`或`RpcClient`对象，取决于`Package Type`字段，对象的序列化方式取决于`Serializer Type`字段。 |

## 使用

### 定义调用接口

```java
package top.guoziyang.rpc.api;

public interface HelloService {
    String hello(String name);
}
```

### 在服务提供侧实现该接口

```java
package top.guoziyang.test;

import top.guoziyang.rpc.api.HelloService;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello, " + name;
    }
}
```

### 编写服务提供者

```java
package top.guoziyang.test;

import top.guoziyang.rpc.api.HelloService;
import top.guoziyang.rpc.serializer.CommonSerializer;
import top.guoziyang.rpc.transport.netty.server.NettyServer;

@ServiceScan
public class NettyTestServer {
    public static void main(String[] args) {
        NettyServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER);
        server.start();
    }
}
```

这里选用 Netty 传输方式，并且指定序列化方式为 Google Protobuf 方式。

### 在服务消费侧远程调用

```java
package top.guoziyang.test;

import top.guoziyang.rpc.api.HelloService;
import top.guoziyang.rpc.serializer.CommonSerializer;
import top.guoziyang.rpc.transport.RpcClient;
import top.guoziyang.rpc.transport.RpcClientProxy;
import top.guoziyang.rpc.transport.netty.client.NettyClient;

public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.KRYO_SERIALIZER, new RoundRobinLoadBalancer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String res = helloService.hello("ziyang");
        System.out.println(res);
    }
}
```

这里客户端也选用了 Netty 的传输方式，序列化方式采用 Kryo 方式，负载均衡策略指定为轮转方式。

### 启动

在此之前请确保 Nacos 运行在本地 `8848` 端口。

首先启动服务提供者，再启动消费者，在消费侧会输出`Hello, ziyang`。

## TODO

- 使用接口方式自动注册服务
- 配置文件

## LICENSE

My-RPC-Framework is under the MIT license. See the [LICENSE](https://github.com/CN-GuoZiyang/My-RPC-Framework/blob/master/LICENSE) file for details.
