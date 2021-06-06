package socket;

import entity.RpcRequest;
import entity.RpcResponse;
import enumeration.PackageType;
import enumeration.RpcError;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import serializer.CommonSerializer;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description
 * @Author Cy
 * @Date 2021/6/2 16:02
 */
@Slf4j
public class ObjectReader {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    public static Object readObject(InputStream in) throws IOException {
        byte[] bytes = new byte[4];
        in.read(bytes);
        int magic = bytesToInt(bytes);
        if(magic != MAGIC_NUMBER){
            log.error("不识别的协议包:{}",magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        in.read(bytes);
        int packageCode = bytesToInt(bytes);
        Class<?> packageClass;
        if(packageCode == PackageType.REQUEST_PACK.getCode()){
            packageClass = RpcRequest.class;
        } else packageClass = RpcResponse.class;
        in.read(bytes);
        int serializerCode = bytesToInt(bytes);
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if(serializer == null){
            log.error("不识别的反序列化器：{}",packageClass);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        in.read(bytes);
        int len = bytesToInt(bytes);
        byte[] b = new byte[len];
        in.read(b);
        return serializer.deserialize(b,packageClass);
    }

    public static int bytesToInt(byte[] src) {
        int value;
        value = ((src[0] & 0xFF)<<24)
                |((src[1] & 0xFF)<<16)
                |((src[2] & 0xFF)<<8)
                |(src[3] & 0xFF);
        return value;
    }
}
