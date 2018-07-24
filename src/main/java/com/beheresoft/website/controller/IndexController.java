package com.beheresoft.website.controller;

import com.beheresoft.website.dict.SystemDict;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author Aladi
 */
@Controller
@RequestMapping("/")
public class IndexController {

    private SystemDict systemDict;

    public IndexController(SystemDict systemDict) {
        this.systemDict = systemDict;
    }

    @RequestMapping("/")
    public ModelAndView index(Pageable page) {
        List<String> folders = this.systemDict.getMetaInfo().getCatalog().catalogs();
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("menu", folders);
        modelAndView.addObject("articles", systemDict.listMetas(page));
        return modelAndView;
    }

}
