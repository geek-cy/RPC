package serializer;

/**
 * 序列化方式
 * @author Cy
 * @date 2021/6/5 20:43
 */
public interface CommonSerializer {

    /**
     * 根据序列化序号选择序列化方式
     */
    static CommonSerializer getSerializer(int code){
        switch (code){
            case 0:
                return new KryoSerializer();
            default: return null;
        }
    }

    /**
     * 序列化
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     */
    Object deserialize(byte[] bytes,Class<?> clazz);

    int getCode();
}
