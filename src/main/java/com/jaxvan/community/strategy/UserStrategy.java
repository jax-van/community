package com.jaxvan.community.strategy;

public interface UserStrategy {
    LoginUserInfo getUser(String code, String state);
    String getSupportedType();
}
