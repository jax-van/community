package com.jaxvan.community.mapper;

import com.jaxvan.community.model.Comment;

public interface CommentExtMapper {
    int incrCommentCountByOne(Long id);

    int incrLikeCount(Comment comment);
}
