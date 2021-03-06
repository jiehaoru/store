package com.jhr.controller;

import com.jhr.entity.User;
import com.jhr.service.UserService;
import com.jhr.utils.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * <br>
 * 标题： 用户登录
 * 描述：
 *
 * @author jhr
 * @create 2019/03/21 10:16
 */
@Controller
@RequestMapping("/lecture")
public class UserController extends BaseController {
    @Autowired
    UserService userService;

    //注册
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody User user) {

        System.out.print("***************************");
       User user1 =userService.selectByName(user.getUsername());
       if (null!=user1){
           return "1";
       }
        //确认密码比对  (Filed1作为确认密码)
        if (user.getPassword().equals(user.getFiled1())) {
            user.setId(Sequence.getInstance().nextId());
            user.setFlag(1);
            user.setCreatetime(new Date());
            user.setAuthorityid(1);//普通权限
            boolean register = userService.register(user);
            if (register){
                return "0";
            }else {
                return "1";
            }
        } else {
            return "1";
        }

    }


    //登录
    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestBody User user,HttpSession session) {

        //如何对密码加密？ MD5算法

        User user1 = userService.login(user);
        //判断用户是否登录成功 user是否是null
        if (user1 != null) {
            //登录成功
            // session 存登录人
            super.setLogin(session,user1);
            return "0";
        } else {
//            request.setAttribute("loginError", "用户名或密码错误");
            return "1";
        }
    }

//    @RequestMapping(value = "/logout")
//    public String logout(HttpSession session) {
//        session.removeAttribute("user");
//        return "redirect:/index.html";
//    }
}
