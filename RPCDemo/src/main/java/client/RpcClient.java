package client;

import entity.RpcRequest;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/24 23:45
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}
