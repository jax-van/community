package com.jaxvan.community.cache;

import com.jaxvan.community.dto.HotTagDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

@Component
public class HotTagCache {
    private static List<String> hotTags = new ArrayList<>();

    public void updateHotTags(Map<String, Integer> priorityMap) {
        // TopN问题，使用小顶堆优先队列排序
        int max = 10;
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);
        priorityMap.forEach((tagName, priority) -> {
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setTagName(tagName);
            hotTagDTO.setPriority(priority);
            if (priorityQueue.size() < max) {
                priorityQueue.add(hotTagDTO);
            } else if (hotTagDTO.compareTo(priorityQueue.peek()) > 0) {
                priorityQueue.remove();
                priorityQueue.add(hotTagDTO);
            }
        });

        // 通过优先队列得到排序结果
        List<String> sortedHotTags = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {
            HotTagDTO hotTagDTO = priorityQueue.poll();
            sortedHotTags.add(0, hotTagDTO.getTagName());
        }

        this.hotTags = sortedHotTags;
    }

    public static List<String> getHotTags() {
        return hotTags;
    }
}
