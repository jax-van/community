package com.jaxvan.community.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;

    private Long notifier;

    private Long outerId;

    private  Integer type;

    private String typeName;

    private Long gmtCreate;

    private Integer status;

    private String notifierName;

    private String outerTitle;
}