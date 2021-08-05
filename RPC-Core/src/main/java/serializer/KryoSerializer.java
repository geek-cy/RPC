package serializer;

import api.HelloObject;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import entity.RpcRequest;
import entity.RpcResponse;
import enums.SerializeCode;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Kryo序列化方式
 * 通过ThreadLocal对象为键读写当前线程的Map
 * @author Cy
 * @date 2021/6/5 20:06
 */
@Slf4j
public class KryoSerializer implements CommonSerializer{
    private static final FastThreadLocal<Kryo> kryoThreadLocal = new FastThreadLocal<Kryo>(){
        @Override
        protected Kryo initialValue() throws Exception {
            log.debug("初始化kryo");
            Kryo kryo = new Kryo();
            kryo.register(RpcRequest.class);
            kryo.register(RpcResponse.class);
            kryo.register(Class[].class);
            kryo.register(Class.class);
            kryo.register(HelloObject.class);
            kryo.register(Object[].class);
            // 关闭循环引用
            kryo.setReferences(false);
            // 开启注册行为
            kryo.setRegistrationRequired(true);
            return kryo;
        }

        @Override
        protected void onRemoval(Kryo value) throws Exception {
            System.out.println("移除kryo");
        }
    };

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (IOException e) {
            log.error("Kryo序列化失败");
            throw new RuntimeException("Kryo序列化失败");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return o;
        } catch (IOException e) {
            log.error("Kryo反序列化失败");
            throw new RuntimeException("Kryo反序列化失败");
        }
    }

    @Override
    public int getCode() {
//        return SerializeCode.KRYO.getCode();
        return SerializeCode.valueOf("KRYO").getCode();
    }
}
