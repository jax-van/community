package com.jaxvan.community.service;

import com.jaxvan.community.dto.PaginationDTO;
import com.jaxvan.community.dto.QuestionDTO;
import com.jaxvan.community.exception.CustomizeErrorCode;
import com.jaxvan.community.exception.CustomizeException;
import com.jaxvan.community.mapper.QuestionExtMapper;
import com.jaxvan.community.mapper.QuestionMapper;
import com.jaxvan.community.mapper.UserMapper;
import com.jaxvan.community.model.Question;
import com.jaxvan.community.model.QuestionExample;
import com.jaxvan.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        // 设置分页并得到总页数好进一步调整
        Integer totalCount = (int) questionMapper.countByExample(null);
        paginationDTO.setPagination(totalCount, page, size);

        // 越界调整
        if (page < 1) {
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }
        paginationDTO.setPage(page);

        // 根据页码和页面大小获取QuestionDTO列表，并放入分页中
        Integer offset = (page - 1) * size;
        if (offset < 0) {
            offset = 0;
        }
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(null, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO listByUserId(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        // 设置分页并得到总页数好进一步调整
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        int totalCount = (int) questionMapper.countByExample(questionExample);
        paginationDTO.setPagination(totalCount, page, size);

        // 越界调整
        if (page < 1) {
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }
        paginationDTO.setPage(page);

        // 根据页码和页面大小获取QuestionDTO列表，并放入分页中
        Integer offset = (page - 1) * size;
        if (offset < 0) {
            offset = 0;
        }
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();

        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            // 创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        } else {
            // 修改
            question.setGmtModified(System.currentTimeMillis());
            int update = questionMapper.updateByPrimaryKeySelective(question);
            if (update != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void increaseViewByOne(Long id) {
        questionExtMapper.increaseViewByOne(id);
    }
}
