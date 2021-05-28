package client;

import entity.RpcRequest;
import serializer.CommonSerializer;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/24 23:45
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);

    void setSerializer(CommonSerializer serializer);
}
