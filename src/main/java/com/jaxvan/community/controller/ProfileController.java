package com.jaxvan.community.controller;

import com.jaxvan.community.dto.PaginationDTO;
import com.jaxvan.community.model.User;
import com.jaxvan.community.service.NotificationService;
import com.jaxvan.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "size", defaultValue = "5") Integer pageSize,
                          HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        PaginationDTO pagination = null;
        // 字面量调用equals方法防止空指针
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            pagination = questionService.listByUserId(user.getId(), pageNum, pageSize);
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            pagination = notificationService.listByUserId(user.getId(), pageNum, pageSize);
        }
        model.addAttribute("pagination", pagination);
        return "profile";
    }
}
