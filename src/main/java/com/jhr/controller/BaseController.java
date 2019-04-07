package com.jhr.controller;

import com.jhr.entity.User;

import javax.servlet.http.HttpSession;




/**
 * 基础控制层
 * 仅供子类使用
 * author:huanglm@2019-03-10 22:02
 */
public class BaseController {
    protected BaseController() {}

    /**
     * 判断用户是否登录
     * 如果登陆返回改用户信息
     * 否则返回null
     * @param session
     * @return
     */
    protected User isLogin(HttpSession session) {
        // 后续根据代码结构确定如何判断是否登录
        return (User) session.getAttribute("loginTag");
    }

    /**
     * 设置用户登陆信息
     * @param session
     * @param user
     */
    protected void setLogin(HttpSession session, User user) {
        session.setAttribute("loginTag", user);
    }

    /**
     * 清除用户登陆信息
     * @param session
     */
    protected void removeLogin(HttpSession session) {
        session.removeAttribute("loginTag");
    }
}
