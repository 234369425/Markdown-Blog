package com.beheresoft.website.advices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Aladi
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public ModelAndView exceptionHanlder(Exception e) {
        ModelAndView modelAndView = new ModelAndView("errors/500");

        return modelAndView;
    }

}
