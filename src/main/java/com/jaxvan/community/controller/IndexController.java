package com.jaxvan.community.controller;

import com.jaxvan.community.dto.PaginationDTO;
import com.jaxvan.community.mapper.UserMapper;
import com.jaxvan.community.model.User;
import com.jaxvan.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
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
                        @RequestParam(value = "pn", defaultValue = "1") Integer pageNum,
                        @RequestParam(value = "size", defaultValue = "5") Integer pageSize) {
        // 从cookie中拿到token，用来查到user
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        PaginationDTO pagination = questionService.list(pageNum, pageSize);
        model.addAttribute("pagination", pagination);
        return "index";
    }

}
