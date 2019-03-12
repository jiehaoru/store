package com.jhr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <br>
 * 标题：
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 22:49
 */
@Controller("/cc")
public class CC {

    @RequestMapping("/index")
    public String  index(){
        return "index.jsp";
    }
}
