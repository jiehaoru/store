package com.jhr.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <br>
 * 标题：
 * 描述：
 *
 * @author jhr
 * @create 2019/03/13 19:10
 */
@Controller
@RequestMapping("/lecture")
public class RootgetController extends BaseController {

//    @GetMapping("/")
//    @ResponseStatus(value =HttpStatus.MOVED_PERMANENTLY)
//    public String root(){
//
//
//        return "redirect:";
//    }

//    @Controller
//    @RequestMapping("/")
//    public class index {
//        @RequestMapping(value = "/", method = RequestMethod.GET)
//        public String index(Model model){
//            return "index";
//        }
//    }

}
