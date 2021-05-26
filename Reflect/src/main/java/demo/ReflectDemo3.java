package demo;

import domain.Person;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/27 0:00
 */
public class ReflectDemo3 {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<Person> personClass = Person.class;
        Constructor<?>[] constructors = personClass.getConstructors();
        System.out.println(Arrays.toString(constructors));
        Constructor<Person> constructor = personClass.getConstructor(String.class, int.class,int.class,String.class);
        System.out.println(constructor);
        // 构造器创建对象
        Person person = constructor.newInstance("张三", 23, 1, "test");
        System.out.println(person);
        // 空参构造创建对象
        Person person1 = personClass.newInstance();
        System.out.println(person1);

        Constructor<?>[] declaredConstructors = personClass.getDeclaredConstructors();
        System.out.println(Arrays.toString(declaredConstructors));
    }
}
