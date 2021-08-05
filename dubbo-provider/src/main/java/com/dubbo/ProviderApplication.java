package com.dubbo;

import com.dubbo.api.DemoService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author Cy
 * @date 2021/6/14 21:55
 */
/*@SpringBootApplication
@EnableDubbo*/
public class ProviderApplication {

    public static void main(String[] args) throws IOException {
//        SpringApplication.run(ProviderApplication.class,args);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/dubbo-provider.xml");
        context.start();
        System.in.read();
    }
}
