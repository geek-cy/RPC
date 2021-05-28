package serializer;



/**
 * @Description
 * @Author Cy
 * @Date 2021/5/25 15:36
 */
public interface CommonSerializer {
    /**
     * 序列化内容
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化内容
     */
    Object deserialize(byte[] bytes,Class<?> clazz);

    /**
     * 序列化工具类型
     */
    int getCode();

    /**
     * 读取序列化工具类型
     */
    static CommonSerializer getByCode(int code){
        switch (code){
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
