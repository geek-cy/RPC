package socket;

import handler.RequestHandler;
import lombok.extern.slf4j.Slf4j;
import registry.ServiceProvider;
import entity.RpcRequest;
import entity.RpcResponse;
import registry.ServiceRegistry;
import serializer.CommonSerializer;

import java.io.*;
import java.net.Socket;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/24 23:01
 */
@Slf4j
public class RequestHandlerThread implements Runnable {

    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceProvider serviceProvider;
    private ServiceRegistry serviceRegistry;
    private CommonSerializer serializer;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try (OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            Object result = requestHandler.handler(rpcRequest);
            RpcResponse<Object> response = RpcResponse.success(result, rpcRequest.getRequestId());
            ObjectWriter.writeObject(outputStream,response,serializer);
        } catch (IOException e) {
            log.error("有错误发生",e);
        }
    }
}
