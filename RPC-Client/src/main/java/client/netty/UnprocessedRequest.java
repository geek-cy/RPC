package client.netty;

import entity.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在调用 sendRpcRequest() 方法时，把 requestId() 和 一个 completableFuture 放入一个 map 中，返回这个 completableFuture 给动态代理的 invoke 方法。
 * invoke 方法调用 completableFuture.get()阻塞 等待 completableFuture 返回执行结果。
 * 入站处理器调用 completableFuture.complete(rpcResponse)
 * 这个方法的含义是：如果执行还没结束，就以指定的值作为其执行结果，并触发依赖它的其他阶段执行。
 * 因为我们并没有交给 completableFuture 任何任务，所以其实就是用事件（channelRead）驱动一下异步工作。
 * 这时 invoke 方法中的 completableFuture.get(); 就可以得到 rpcResponse 了。
 */
@Slf4j
public class UnprocessedRequest {
    private static final Map<String, CompletableFuture<RpcResponse<Object>>> UNPROCESSED_RESPONSE_FUTURES = new ConcurrentHashMap<>();

    public void put(String requestId,CompletableFuture<RpcResponse<Object>> future){
        UNPROCESSED_RESPONSE_FUTURES.put(requestId, future);
    }

    public void complete(RpcResponse<Object> rpcResponse){
        CompletableFuture<RpcResponse<Object>> future = UNPROCESSED_RESPONSE_FUTURES.remove(rpcResponse.getRequestId());
        if(future != null) {
            future.complete(rpcResponse);
            log.info("得到rpcResponse");
        }
    }
}
