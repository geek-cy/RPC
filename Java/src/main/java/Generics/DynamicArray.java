package Generics;

import lombok.ToString;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 测试泛型上界
 * @Author Cy
 * @Date 2021/6/4 20:04
 */
@ToString
public class DynamicArray<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private int size;
    private Object[] elementData;
    public DynamicArray() {
        this.elementData = new Object[DEFAULT_CAPACITY];
    }
    private void ensureCapacity(int minCapacity) {
        int oldCapacity = elementData.length;
        if(oldCapacity >= minCapacity){
            return;
        }
        int newCapacity = oldCapacity * 2;
        if(newCapacity < minCapacity)
            newCapacity = minCapacity;
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
    public void add(E e) {
        ensureCapacity(size + 1);
        elementData[size++] = e;
    }
    public E get(int index) {
        return (E)elementData[index];
    }
    public int size() {
        return size;
    }
    public E set(int index, E element) {
        E oldValue = get(index);
        elementData[index] = element;
        return oldValue;
    }

    public <T extends E> void addAll(DynamicArray<T> c) {
        for(int i=0; i<c.size; i++){
            add(c.get(i));
        }
    }

    @Test
    public void test(){
        DynamicArray<Number> numbers = new DynamicArray<>();
        DynamicArray<Integer> ints = new DynamicArray<>();
        DynamicArray<Double> intD = new DynamicArray<>();
        ints.add(100);
        ints.add(34);
        intD.add(55.5);
        numbers.addAll(intD);
        numbers.addAll(ints);
        System.out.println(numbers.toString());
    }
}
