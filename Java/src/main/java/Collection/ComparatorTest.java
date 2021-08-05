package Collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Cy
 * @date 2021/6/19 17:43
 */
public class ComparatorTest {
    public static void main(String[] args) {

        List<Student> students = new ArrayList<>();
        Student student = new Student("com", 98.5);
        students.add(student);
        student = new Student("ban",87);
        students.add(student);
        student = new Student("andy",95);
        students.add(student);
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return 1; // return 0 原序
//                return -1; // 反序
//                return (int) (o2.score- o1.score) // 字典升序
//                return (int) (o2.score- o1.score);// 字典降序
//                Integer s1 = (int)o1.score;
//                Integer s2 = (int)o2.score;
//                return s1.compareTo(s2);// 升序
            }
        });

        for(Student stu : students){
            System.out.println(stu.name + "" + stu.score);
        }
    }

    // 它的创建是不需要依赖外围类的创建，不能使用任何外围类的非 static 成员变量和方法。
    // 具有延迟初始化的好处，而且由 JVM 提供了对线程安全的支持
    public static class Student{
        String name;
        double score;

        public Student(String name,double score){
            this.name = name;
            this.score = score;
        }
    }
}

