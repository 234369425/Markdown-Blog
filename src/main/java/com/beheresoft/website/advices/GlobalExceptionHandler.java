package com.beheresoft.website.advices;

import com.beheresoft.website.exception.ArticleNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Aladi
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler({ArticleNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView articleHandler(ArticleNotFoundException notFound) {
        ModelAndView modelAndView = new ModelAndView("errors/article404");
        log.error("article {} not found !", notFound.getHashcode());
        return modelAndView;
    }

    @ExceptionHandler({Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) {
        ModelAndView modelAndView = new ModelAndView("errors/500");
        e.printStackTrace();
        log.error("uri : {} , exception: {}", request.getRequestURI(), e.getCause());
        return modelAndView;
    }

}
