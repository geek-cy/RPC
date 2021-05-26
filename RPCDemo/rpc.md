
### RPC原理

客户端和服务端都可以访问到通用的接口，
但是只有服务端有这个接口的实现类，
客户端调用这个接口的方式，是通过网络传输，告诉服务端我要调用这个接口，
服务端收到之后找到这个接口的实现类，并且执行，将执行的结果返回给客户端，作为客户端调用接口方法的返回值。

![](https://cdn.jsdelivr.net/gh/geek-cy/img/RPC/aHR0cHM6Ly9jbi1ndW96aXlhbmcuZ2l0aHViLmlvL015LVJQQy1GcmFtZXdvcmsvaW1nL1JQQyVFNiVBMSU4NiVFNiU5RSVCNiVFNiU4MCU5RCVFOCVCNyVBRi5qcGVn.jpg)


### 问题
1、ReplayingDecoder
2、

### 碰到的困难
1、Json类型转换，类型擦除

#### 面试问题
zookeeper的主节点选举算法
Netty线程模型，RPC的整体结构，Zookeeper如何保证数据的一致性
RPC调用过程中若出错了怎么办
RPC心跳检测是怎么做的？服务注册、发现、注销如何实现的
RPC负载均衡算法随机和轮询的优缺点？
Netty的select过程为啥效率高
什么是零拷贝(mmap,sendfile)
RPC写了哪些功能
RPC中序列化是怎么实现的