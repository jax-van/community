package com.jaxvan.community.service;

import com.jaxvan.community.dto.CommentDTO;
import com.jaxvan.community.enums.CommentTypeEnum;
import com.jaxvan.community.enums.NotificationStatusEnum;
import com.jaxvan.community.enums.NotificationTypeEnum;
import com.jaxvan.community.exception.CustomizeErrorCode;
import com.jaxvan.community.exception.CustomizeException;
import com.jaxvan.community.mapper.*;
import com.jaxvan.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PRAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            // 回复评论
            Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (parentComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            // 插入评论平使得评论数加一
            commentMapper.insertSelective(comment);
            commentExtMapper.incrCommentCountByOne(comment.getParentId());
            // 创建通知
            createNotification(commentator, parentComment);
        } else {
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insertSelective(comment);
            questionExtMapper.incrCommentCountByOne(comment.getParentId());
            // 建立通知
            createNotification(commentator, question);
        }
    }

    private void createNotification(User commentator, Comment parentComment) {
        // 回复人是自己则不创建通知
        if (commentator.getId() == parentComment.getCommentator()) {
            return;
        }
        Notification notification = new Notification();
        notification.setNotifierName(commentator.getName());
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifier(commentator.getId());
        notification.setType(NotificationTypeEnum.REPLY_COMMENT.getType());
        notification.setOuterId(parentComment.getParentId());
        notification.setOuterTitle(parentComment.getContent());
        notification.setReceiver(parentComment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.insertSelective(notification);
    }

    private void createNotification(User commentator, Question question) {
        // 回复人是自己则不创建通知
        if (commentator.getId() == question.getCreator()) {
            return;
        }
        Notification notification = new Notification();
        notification.setNotifierName(commentator.getName());
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifier(commentator.getId());
        notification.setType(NotificationTypeEnum.REPLY_QUESTION.getType());
        notification.setOuterId(question.getId());
        notification.setOuterTitle(question.getTitle());
        notification.setReceiver(question.getCreator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.insertSelective(notification);
    }

    public List<CommentDTO> listByParentId(Long id, CommentTypeEnum commentTypeEnum) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(commentTypeEnum.getType());
        // 按时间倒叙排列
        example.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(example);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }

        // 流操作 lambda表达式 函数式编程
        // 获取去重评论人并转化为 List
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator())
                .collect(Collectors.toSet());
        ArrayList<Long> userIds = new ArrayList<>(commentators);

        // 获取评论人并转换为 Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(user -> user.getId(), user -> user));

        // 转换 comment 为 commentDTO
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            commentDTOs.add(commentDTO);
        }
        // 这时候用stream并没有简洁代码，反而没有增强for循环可读性高
        //    List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
        //    CommentDTO commentDTO = new CommentDTO();
        //    BeanUtils.copyProperties(comment, commentDTO);
        //    commentDTO.setUser(userMap.get(comment.getCommentator()));
        //    return commentDTO;
        //}).collect(Collectors.toList());
        return commentDTOs;
    }

    public void incrLikeCount(Long id) {
        Comment comment = new Comment();
        if (id < 0) {
            comment.setLikeCount(-1l);
            id = -id;
        } else {
            comment.setLikeCount(1l);
        }

        comment.setId(id);
        commentExtMapper.incrLikeCount(comment);
    }
}
