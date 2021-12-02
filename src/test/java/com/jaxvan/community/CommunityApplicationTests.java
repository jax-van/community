package com.jaxvan.community;

import com.jaxvan.community.provider.AliCouldService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommunityApplicationTests {
    @Autowired
    AliCouldService aliCouldService;

    @Test
    void contextLoads() {
        aliCouldService.upload();
    }
}
