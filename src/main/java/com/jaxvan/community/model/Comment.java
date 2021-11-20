package com.jaxvan.community.model;


public class Comment {

  private long id;
  private long parentId;
  private long type;
  private long commentator;
  private long gmtCreate;
  private long gmtModified;
  private long likeCount;
  private String content;
  private long commentCount;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getParentId() {
    return parentId;
  }

  public void setParentId(long parentId) {
    this.parentId = parentId;
  }


  public long getType() {
    return type;
  }

  public void setType(long type) {
    this.type = type;
  }


  public long getCommentator() {
    return commentator;
  }

  public void setCommentator(long commentator) {
    this.commentator = commentator;
  }


  public long getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(long gmtCreate) {
    this.gmtCreate = gmtCreate;
  }


  public long getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(long gmtModified) {
    this.gmtModified = gmtModified;
  }


  public long getLikeCount() {
    return likeCount;
  }

  public void setLikeCount(long likeCount) {
    this.likeCount = likeCount;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public long getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(long commentCount) {
    this.commentCount = commentCount;
  }

}
