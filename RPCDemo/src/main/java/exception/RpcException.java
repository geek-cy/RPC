package exception;

import enumeration.RpcError;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/24 22:21
 */
public class RpcException extends RuntimeException{
    public RpcException(RpcError error,String detail){
        super(error.getMessage() + ":" +detail);
    }

    public RpcException(String message,Throwable cause){
        super(message,cause);
    }

    public RpcException(RpcError error){
        super(error.getMessage());
    }
}
