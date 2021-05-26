package serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import enumeration.SerializerCode;
import lombok.extern.slf4j.Slf4j;
import entity.RpcRequest;

import java.io.IOException;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/25 15:38
 */
@Slf4j
public class JsonSerializer implements CommonSerializer{

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("序列化出现错误:{}",e.getMessage());
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            Object o = objectMapper.readValue(bytes, clazz);
            if(o instanceof RpcRequest){
                o = handlerRequest(o);
            }
            return o;
        } catch (IOException e) {
            log.error("反序列化出现错误:{}",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用JSON序列化和反序列化为Object数组，会丢失对象的类型信息，无法保证反序列化后仍然为原实例类型，需要判断
     */
    private Object handlerRequest(Object o) throws IOException{
        RpcRequest rpcRequest = (RpcRequest) o;
        for(int i = 0;i < rpcRequest.getParamTypes().length;i++){
            Class<?> clazz = rpcRequest.getParamTypes()[i];
            // isAssignableFrom()方法是从类继承的角度去判断，instanceof关键字是从实例继承的角度去判断。
            // isAssignableFrom()方法是判断是否为某个类的父类，instanceof关键字是判断是否某个类的子类。
            if(clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())){
                objectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
            }
        }
        return rpcRequest;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }
}
