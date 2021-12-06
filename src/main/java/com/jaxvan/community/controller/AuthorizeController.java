package com.jaxvan.community.controller;

import com.jaxvan.community.model.User;
import com.jaxvan.community.service.UserService;
import com.jaxvan.community.strategy.LoginUserInfo;
import com.jaxvan.community.strategy.UserStrategy;
import com.jaxvan.community.strategy.UserStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserStrategyFactory userStrategyFactory;

    @GetMapping("/callback/{type}")
    public String callback(@PathVariable(name = "type") String type,
                           @RequestParam(name = "code") String code,
                           @RequestParam(name = "state", required = false) String state,
                           HttpServletResponse response) {
        UserStrategy userStrategy = userStrategyFactory.getStrategy(type);
        LoginUserInfo loginUserInfo = userStrategy.getUser(code, state);

        // 浏览器 Cookie 存储 token，这样不用每次结束会话就要重新登录了
        if (loginUserInfo != null && loginUserInfo.getId() != null) {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(loginUserInfo.getName());
            user.setAccountId(loginUserInfo.getId().toString());
            user.setAvatarUrl(loginUserInfo.getAvatarUrl());
            user.setType(type);
            userService.createOrUpdateUser(user);
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/";
        } else {
            // 登录失败
            log.error("登录失败 {}", loginUserInfo);
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        // 清除session cookie
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");// 项目所有目录均有效
        response.addCookie(cookie);
        return "redirect:/";
    }
}
