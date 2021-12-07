package com.jaxvan.community.advice;

import com.alibaba.fastjson.JSON;
import com.jaxvan.community.dto.ResponseDTO;
import com.jaxvan.community.exception.CustomizeErrorCode;
import com.jaxvan.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestControllerAdvice
public class CustomizeControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleControllerException(Throwable ex, Model model,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            ResponseDTO responseDTO = null;
            // 返回JSON
            if (ex instanceof CustomizeException) {
                responseDTO = ResponseDTO.errorOf((CustomizeException) ex);
            } else {
                responseDTO = ResponseDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(responseDTO));
                writer.close();
            } catch (IOException e) {}
            return null;
        } else {
            // 错误页面跳转
            if (ex instanceof CustomizeException) {
                model.addAttribute("message", ex.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}