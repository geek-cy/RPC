package com.dubbo.spi;

import org.apache.dubbo.common.URL;

/**
 * @author Cy
 * @date 2021/6/21 16:03
 */
public class ClusterImpl implements Cluster{

    private Person person;

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public void say() {
        System.out.println("hello");
    }

    @Override
    public void name(URL url) {
        String name = person.getName(url);
        System.out.println(name);
    }
}
