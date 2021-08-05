/**
 * @author Cy
 * @date 2021/6/10 18:54
 */
public @interface MyAnno {
    int age();
    String name() default "name";

//    int value();
    Person p1();
    YourAnno y();

    String[] str();
}
