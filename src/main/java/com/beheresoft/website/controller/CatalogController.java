package com.beheresoft.website.controller;

import com.beheresoft.website.dict.SystemDict;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/catalog/")
public class CatalogController {

    private SystemDict systemDict;

    public CatalogController(SystemDict systemDict) {
        this.systemDict = systemDict;
    }

    @RequestMapping("list/{id}")
    public ModelAndView list(@PathVariable("id") Long hashCode, Pageable page) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("articles",systemDict.listCatalogMetas(hashCode, page));
        return modelAndView;
    }

}
