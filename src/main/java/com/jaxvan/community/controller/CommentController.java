package com.jaxvan.community.controller;

import com.jaxvan.community.dto.CommentCreateDTO;
import com.jaxvan.community.dto.CommentDTO;
import com.jaxvan.community.dto.ResponseDTO;
import com.jaxvan.community.enums.CommentTypeEnum;
import com.jaxvan.community.exception.CustomizeErrorCode;
import com.jaxvan.community.model.Comment;
import com.jaxvan.community.model.User;
import com.jaxvan.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping(value = "/comment")
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResponseDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResponseDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        commentService.insert(comment);
        return ResponseDTO.okOf();
    }

    @GetMapping("/comment/{id}")
    public ResponseDTO comments(@PathVariable("id") Long id) {
        List<CommentDTO> commentDTOS =
                commentService.listByParentId(id, CommentTypeEnum.COMMENT);
        return ResponseDTO.okOf(commentDTOS);
    }
}
