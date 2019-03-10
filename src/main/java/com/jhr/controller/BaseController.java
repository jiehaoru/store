package com.jhr.controller;

import javax.servlet.http.HttpSession;

/**
 * 基础控制层
 * 仅供子类使用
 * author:huanglm@2019-03-10 22:02
 */
public class BaseController {
    protected BaseController() {}

    protected boolean isLogin(HttpSession session) {
        // 后续根据代码结构确定如何判断是否登录
        return true;
    }
}
