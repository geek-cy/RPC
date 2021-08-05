package com.dubbo.spi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

/**
 * @author Cy
 * @date 2021/7/4 15:23
 */
@SPI// 不会从Spring容器去拿
public interface Person {

    @Adaptive
    String getName(URL url);
}
