package factory;

import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Cy
 * @date 2021/6/8 23:21
 */
@NoArgsConstructor
public class SingletonFactory {
    private static final Map<String,Object> OBJECT_MAP = new ConcurrentHashMap<>();

    public static <T> T getInstance(Class<T> clazz){
        if (clazz == null) throw new IllegalArgumentException();
        String key = clazz.toString();
        if(OBJECT_MAP.containsKey(key)) return clazz.cast(OBJECT_MAP.get(key));
        try {
            T t = clazz.getDeclaredConstructor().newInstance();
            OBJECT_MAP.put(key,t);
            return t;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
