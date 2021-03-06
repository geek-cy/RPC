package client;

import entity.RpcRequest;

/**
 * 声明被监听的行为
 * @author Cy
 * @date 2021/6/6 0:58
 */
public interface RpcClient {
    /**
     * 通过Netty客户端来发送请求并获得响应信息
     */
    Object sendRequest(RpcRequest rpcRequest);

    /**
     * 关闭连接
     */
    void close();
}
