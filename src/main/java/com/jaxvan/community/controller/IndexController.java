package com.jaxvan.community.controller;

import com.jaxvan.community.cache.HotTagCache;
import com.jaxvan.community.dto.PaginationDTO;
import com.jaxvan.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "tag", required = false) String tag) {
        PaginationDTO pagination = questionService.list(search, tag, page, size);
        List<String> hotTags = HotTagCache.getHotTags();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);

        model.addAttribute("hotTags", hotTags);
        model.addAttribute("tag", tag);

        return "index";
    }

}
