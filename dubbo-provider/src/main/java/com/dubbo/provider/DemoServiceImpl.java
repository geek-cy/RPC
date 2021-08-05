package com.dubbo.provider;

import com.dubbo.api.DemoService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Cy
 * @date 2021/7/4 16:14
 */
@Slf4j
public class DemoServiceImpl implements DemoService {
    @Override
    public String hello() throws InterruptedException {
        return "调用成功";
    }
}
