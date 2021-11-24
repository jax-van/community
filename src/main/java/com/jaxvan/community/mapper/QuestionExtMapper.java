package com.jaxvan.community.mapper;

public interface QuestionExtMapper {
    int incrViewByOne(Long id);
    int incrCommentViewByOne(Long id);
}
