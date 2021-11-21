package com.jaxvan.community.controller;

import com.jaxvan.community.dto.PaginationDTO;
import com.jaxvan.community.mapper.UserMapper;
import com.jaxvan.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer pageSize) {
        PaginationDTO pagination = questionService.listByUserId(page, pageSize);
        model.addAttribute("pagination", pagination);
        return "index";
    }

}
