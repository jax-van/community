package com.jaxvan.community.dto;

import com.jaxvan.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {

  private Long id;
  private String title;
  private String description;
  private Long gmtCreate;
  private Long gmtModified;
  private Long creator;
  private Integer commentCount;
  private Integer viewCount;
  private Integer likeCount;
  private String tag;
  private Long sticky;
  private User user;
}
