package com.beheresoft.website.controller;

import com.beheresoft.website.dict.ViewerCounter;
import com.beheresoft.website.exception.ArticleNotFoundException;
import com.beheresoft.website.service.MarkDownArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @author Aladi
 */
@Controller
@RequestMapping("/article/md/")
public class MarkDownArticleController {

    private MarkDownArticleService markDownArticleService;
    private ViewerCounter counter;

    public MarkDownArticleController(MarkDownArticleService articleService, ViewerCounter counter) {
        this.markDownArticleService = articleService;
        this.counter = counter;
    }

    @RequestMapping("/get/{hashcode}")
    public ModelAndView get(@PathVariable("hashcode") Long hashCode) throws ArticleNotFoundException {
        ModelAndView modelAndView = new ModelAndView("article");
        modelAndView.addObject("article", this.markDownArticleService.search(hashCode));
        modelAndView.addObject("viewers", counter.add(hashCode));
        return modelAndView;
    }

    @RequestMapping("/list")
    public ModelAndView list() throws ArticleNotFoundException {
        ModelAndView modelAndView = new ModelAndView("markdown");
        modelAndView.addObject("value", markDownArticleService.search(111));
        return modelAndView;
    }


    @RequestMapping("/about.html")
    public ModelAndView about() throws ArticleNotFoundException {
        ModelAndView modelAndView = new ModelAndView("about");
        modelAndView.addObject("content", this.markDownArticleService.getAbout());
        return modelAndView;
    }

}
