package com.jhr.serviceImpl;

import com.jhr.dao.UserMapper;
import com.jhr.entity.User;
import com.jhr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <br>
 * 标题：
 * 描述：
 *
 * @author jhr
 * @create 2019/03/21 10:54
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper mapper;

    //注册
    @Override
    public boolean register(User user) {
        int row = 0;
        row = mapper.insert(user);
        return row>0;
    }

    //激活
//    @Override
//    public void mailActivate(String activeCode) {
//
//        mapper.mailActivate(activeCode);
//    }

    //检测用户姓名是否已存在
    @Override
    public boolean checkUserName(String username) {
        User user=new User();
        user.setUsername(username);
        List<User> users = mapper.selectUserBy(user);
        if(users.size()>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public User login(String username, String password) {

        User user = mapper.selectByName(username);
        if(password.equals(user.getPassword())){
            return user;
        }
        return null;
    }

    @Override
    public User selectByName(String username) {
        return mapper.selectByName(username);
    }
}
