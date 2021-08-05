package com.dubbo.spi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

/**
 * @author Cy
 * @date 2021/7/4 17:50
 */
@SPI("test")
public interface Test {

    @Adaptive
    String test(URL url);
}
