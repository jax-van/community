package com.jaxvan.community.service;

import com.jaxvan.community.dto.PaginationDTO;
import com.jaxvan.community.dto.QuestionDTO;
import com.jaxvan.community.mapper.QuestionMapper;
import com.jaxvan.community.mapper.UserMapper;
import com.jaxvan.community.model.Question;
import com.jaxvan.community.model.User;
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

    public PaginationDTO listByUserId(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        // 设置分页并得到总页数好进一步调整
        Integer totalCount = questionMapper.count();
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
        List<Question> questionList = questionMapper.list(size, offset);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO listByUserId(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        // 设置分页并得到总页数好进一步调整
        Integer totalCount = questionMapper.countByUserId(userId);
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
        List<Question> questionList = questionMapper.listByUserId(userId, size, offset);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }
}
