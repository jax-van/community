package com.jaxvan.community.mapper;

import com.jaxvan.community.dto.QuestionQueryDTO;
import com.jaxvan.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incrViewByOne(Long id);
    int incrCommentCountByOne(Long id);
    List<Question> selectRelated(Question question);
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);
    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}
