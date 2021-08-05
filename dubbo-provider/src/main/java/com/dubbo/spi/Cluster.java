package com.dubbo.spi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.rpc.cluster.support.FailoverCluster;

/**
 * @author Cy
 * @date 2021/6/21 15:59
 */
@SPI
public interface Cluster {
    void say();

    void name(URL url);
}
