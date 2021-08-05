package com.dubbo.consumer;

import com.dubbo.api.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Cy
 * @date 2021/6/14 22:10
 */
@Component
public class UserInit implements CommandLineRunner {

    @Reference(version = "1.0")
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(userService.selectUserById(1));
    }
}
