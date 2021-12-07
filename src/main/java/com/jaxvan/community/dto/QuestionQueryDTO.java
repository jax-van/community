package com.jaxvan.community.dto;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private int size;
    private int offset;
    private String search;
    private String tag;
    private String sort;
}
