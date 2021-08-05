package com.dubbo.spi;

import org.apache.dubbo.common.URL;

/**
 * @author Cy
 * @date 2021/7/4 18:11
 */
public class TestWrapper implements Test{

    private Test test;

    // 必须写
    public TestWrapper(Test test){
        this.test = test;
    }

    @Override
    public String test(URL url) {
        System.out.println("wrapper");
        return test.test(url);
    }
}
