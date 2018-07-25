package com.beheresoft.website.controller;

import com.beheresoft.website.exception.ArticleNotFoundException;
import com.beheresoft.website.service.MarkDownArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Aladi
 */
@Controller
@RequestMapping("/article/md/")
public class MarkDownArticleController {

    private MarkDownArticleService markDownArticleService;

    public MarkDownArticleController(MarkDownArticleService articleService) {
        this.markDownArticleService = articleService;
    }

    @RequestMapping("/get/{hashcode}")
    public ModelAndView get(@PathVariable("hashcode") Long hashCode) throws ArticleNotFoundException {
        ModelAndView modelAndView = new ModelAndView("article");
        modelAndView.addObject("content", this.markDownArticleService.search(hashCode));
        return modelAndView;
    }

    @RequestMapping("/list")
    public ModelAndView list() throws ArticleNotFoundException {
        ModelAndView modelAndView = new ModelAndView("markdown");
        modelAndView.addObject("value", markDownArticleService.search(111));
        return modelAndView;
    }


    @RequestMapping("/about.html")
    public ModelAndView about() {
        ModelAndView modelAndView = new ModelAndView("about");
        modelAndView.addObject("content", "");
        return modelAndView;
    }

}
