package com.jaxvan.community.strategy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserStrategyFactory {

    // 接口集合使用@Autowired注入子类
    @Autowired
    private List<UserStrategy> userStrategies;

    public UserStrategy getStrategy(String type) {
        // 如果是工程中存在的策略类型则返回相应策略
        for (UserStrategy userStrategy : userStrategies) {
            // 用 util 比较防止空指针
            if (StringUtils.equals(type, userStrategy.getSupportedType())) {
                return userStrategy;
            }
        }
        return null;
    }
}
