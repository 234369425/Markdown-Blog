package com.beheresoft.website.extension.admire;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Aladi
 */
@Controller
@RequestMapping("/admire")
public class AdmireController {

    @RequestMapping("/add/{hashcode}")
    public Object add(@PathVariable("hashcode") Long hashcode, HttpServletRequest request) {
        return "";
    }


}
