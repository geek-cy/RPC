### 反射：框架设计的灵魂
将类的各个部分封装为其他对象，如成员变量、构造方法、成员方法
优点：可以在程序运行过程中操作这些对象，可以解耦提高程序可扩展性

#### 获取Class对象三种方式
1、Class.forName("全类名"): 将字节码文件加载进内存，返回class对象
* 多用于配置文件，将类名定义在配置文件中。读取文件，加载类

2、类名.class:通过类名的属性class获取
* 多用于参数的传递

3、对象.getClass():getClass()方法在Object类中定义的
* 多用于对象的获取字节码的方式

注意同一个字节码文件在一次程序运行过程中，只会被加载一次，不论通过哪一种方式获取的Class对象都是同一个

#### Class对象功能：
1、获取成员变量们
* Field[] getFields():获取所有public修饰的成员变量
* Field getField(String name):获取指定的public修饰的成员变量
* Field[] getDeclaredFields():获取所有的成员变量
* Field getDeclaredField(String name):获取指定的成员变量

2、获取构造方法们
* Constructor<?>[] getConstructors() 获取公共构造方法的 Constructor 对象数
* Constructor<T> getConstructor(类<?>... parameterTypes) 获取指定公共构造方法的 Constructor 对象
* Constructor<?>[] getDeclaredConstructors() 获取所有的构造方法的 Constructor 对象数
* Constructor<T> getDeclaredConstructor(类<?>... parameterTypes) 获取指定构造方法的 Constructor 对象

3、获取成员方法们
* Method[] getMethods()
* Method getMethod(String name,类<?>... parameterTypes)
* Method[] getDeclaredMethods()
* Method getDeclaredMethod(String name,类<?>... parameterTypes)

4、获取类名
* String getName()

#### Field
设置值:voidforName set(Object obj,Object value)  
获取值:get(Object obj)  
忽略访问权限修饰符的安全检查:setAccessible(true)

#### Constructor
创建对象：T newInstance(Object... initargs)  
空参创建对象:Class.newInstance()

#### Method
执行方法:Object invoke(Object obj,Object... args)


