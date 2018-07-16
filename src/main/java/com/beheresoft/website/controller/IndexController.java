package com.beheresoft.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Aladi
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping("/")
    public ModelAndView index(){

        return new ModelAndView("index");
    }

}
