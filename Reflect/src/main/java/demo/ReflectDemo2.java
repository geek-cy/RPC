package demo;

import domain.Person;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/26 23:37
 */
public class ReflectDemo2 {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Class<Person> personClass = Person.class;
        Field[] fields = personClass.getFields();
        Field a = personClass.getField("a");
        System.out.println(Arrays.toString(fields));
        System.out.println(a);
        Person person = new Person();
        // 获取a的值
        Object o = a.get(person);
        // 设置a的值
        a.set(person,3);
        System.out.println(o);
        System.out.println(person);

        Field[] fields1 = personClass.getDeclaredFields();
        System.out.println(Arrays.toString(fields1));

        Field name = personClass.getDeclaredField("name");
        name.setAccessible(true);// 暴力反射，忽略访问权限修饰符的安全检查
        Object o1 = name.get(person);
        System.out.println(o1);
        name.set(person,"3");
        System.out.println(person);
    }
}
