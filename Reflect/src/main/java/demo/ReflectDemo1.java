package demo;

import domain.Person;

/**
 * 获取Class对象的方式
 * @Author Cy
 * @Date 2021/5/26 23:16
 */
public class ReflectDemo1 {
    public static void main(String[] args) throws ClassNotFoundException {
        // 获取对象的方式一：
        Class<?> aClass = Class.forName("domain.Person");
        System.out.println(aClass);
        // 获取对象的方式二
        Class<Person> aClass1 = Person.class;
        System.out.println(aClass1);
        // 获取对象方式三
        Person person = new Person();
        Class<? extends Person> aClass2 = person.getClass();
        System.out.println(aClass2);
    }
}
