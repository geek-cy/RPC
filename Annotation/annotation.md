### 注解
#### 用途
1、编写文档
2、代码分析
3、编译检查

#### 自定义注解
```
// 格式
public @interface Name{
    属性列表
}
// 本质
public interface MyAnno extends java.lang.annotation.Annotation{}
```
本质：就是个接口，默认继承Annotation
属性：接口中的抽象方法

* 要求:属性的返回值类型:基本数据类型，String,枚举，注解，以上类型的数组

其他类在使用注解时需要赋值(除非注解使用了default)
若只有一个属性(value)需要赋值，类中可以省略
若数组中只有一个值就不需要{}

#### 元注解
@Target:描述注解能作用的位置
@Retention:描述注解被保留的阶段
@Documented:描述注解是否被抽取到api文档中
@Inherited:描述注解是否被子类继承