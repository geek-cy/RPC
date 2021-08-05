package com.dubbo.provider;

import com.dubbo.api.User;
import com.dubbo.api.UserService;
import org.apache.dubbo.config.annotation.Service;


/**
 * @author Cy
 * @date 2021/6/14 22:08
 */
@Service(version = "1.0")// 有助于灰度发布
public class UserServiceImpl implements UserService {


    @Override
    public User selectUserById(Integer id) {
        User user = new User();
        user.setId(id);
        user.setName("dubbo");
        return user;
    }
}
