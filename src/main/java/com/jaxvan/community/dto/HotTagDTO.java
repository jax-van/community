package com.jaxvan.community.dto;

import lombok.Data;

@Data
public class HotTagDTO implements Comparable {
    private String tagName;
    private Integer priority;

    @Override
    public int compareTo(Object o) {
        return priority - ((HotTagDTO) o).getPriority();
    }
}
