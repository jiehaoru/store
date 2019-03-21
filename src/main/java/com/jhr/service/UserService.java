package com.jhr.service;

import com.jhr.entity.User;

/**
 * <br>
 * 标题：
 * 描述：
 *
 * @author jhr
 * @create 2019/03/21 10:18
 */
public interface UserService {
    boolean register(User user);

//    void mailActivate(String activeCode);

    boolean checkUserName(String username);

    User login(String username, String password);


    User selectByName(String username);
}
