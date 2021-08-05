package com.dubbo.spi;

import org.apache.dubbo.common.URL;

/**
 * @author Cy
 * @date 2021/7/4 17:51
 */
public class TestImpl implements Test{
    @Override
    public String test(URL url) {
        return url.getParameter("p");
    }
}
