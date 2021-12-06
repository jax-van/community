package com.jaxvan.community.schedule;

import com.jaxvan.community.cache.HotTagCache;
import com.jaxvan.community.mapper.QuestionMapper;
import com.jaxvan.community.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class HotTagTasks {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private HotTagCache hotTagCache;

    private static List<String> hotTags;

    @Scheduled(fixedRate = 5000)
    public void hotTag() {
        List<Question> questions = questionMapper.selectByExample(null);
        Map<String, Integer> priorityMap = new HashMap<>();

        // 遍历所有问题计算每个标签的优先级，priority = 5 * 标签出现次数 + 带有该标签的问题的评论数
        for (Question question : questions) {
            for (String tag : question.getTag().split(",")) {
                Integer priority = priorityMap.get(tag);
                if (priority == null) {
                    priorityMap.put(tag, 5 + question.getCommentCount());
                } else {
                    priorityMap.put(tag, priority + 5 + question.getCommentCount());
                }
            }
        }

        hotTagCache.updateHotTags(priorityMap);


    }

    public static List<String> getHotTags() {
        return hotTags;
    }
}