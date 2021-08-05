package com.dubbo;

import com.dubbo.api.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Cy
 * @date 2021/6/14 22:10
 */
//@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) throws InterruptedException {
//        SpringApplication.run(ProviderApplication.class,args);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/dubbo-consumer.xml");
        context.start();
        DemoService demoService = (DemoService)context.getBean("demoService"); // 获取远程服务代理
        String hello = demoService.hello();// 执行远程方法
        System.out.println(hello);
        Thread.sleep(5000000);
    }

}
