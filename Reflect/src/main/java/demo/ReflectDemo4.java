package demo;

import domain.Person;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/27 0:07
 */
public class ReflectDemo4 {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<Person> personClass = Person.class;
        // 获取指定名称的方法
        Method method = personClass.getMethod("eat");
        Person person = new Person();
        method.invoke(person);

        Method method1 = personClass.getMethod("eat", String.class);
        method1.invoke(person,"饭");

        // 获取所有public修饰的方法
        Method[] methods = personClass.getMethods();
        for(Method m:methods){
            String name = m.getName();
            System.out.println(m);
            System.out.println(name);
        }

        // 获取类名
        String name = personClass.getName();
        System.out.println(name);
    }
}
