package com.kuang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wonders on 2020-07-17 09:36
 */
@Controller
public class IndexController {


    @RequestMapping({"/","index"})
    public String index(){
        return "index";
    }
}