package com.dubbo.spi;

import org.apache.dubbo.common.URL;

/**
 * @author Cy
 * @date 2021/7/4 15:24
 */
public class PersonImpl implements Person{

    @Override
    public String getName(URL url) {
        return "url";
    }
}
