package com.jaxvan.community.advice;

import com.jaxvan.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizeControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleControllerException(Throwable ex, Model model) {
        if (ex instanceof CustomizeException) {
            System.out.println(ex.getMessage());
            model.addAttribute("message", ex.getMessage());
            model.addAttribute("aaa", "1233");
        } else {
            model.addAttribute("message", "服务器异常");
        }
        return new ModelAndView("error");
    }


}