package com.jaxvan.community.strategy;

import com.jaxvan.community.dto.AccessTokenDTO;
import com.jaxvan.community.provider.GiteeProvider;
import com.jaxvan.community.provider.dto.GiteeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GiteeUserStrategy implements UserStrategy {

    @Autowired
    private GiteeProvider giteeProvider;

    @Value("${gitee.client.id}")
    private String clientId;

    @Value("${gitee.client.secret}")
    private String clientSecret;

    @Value("${gitee.redirect.uri}")
    private String redirectUri;

    @Override
    public LoginUserInfo getUser(String code, String state) {
        // 得到access token，再通过其获取用户资料
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = giteeProvider.getAccessToken(accessTokenDTO);
        GiteeUser githubUser = giteeProvider.getUser(accessToken);

        // 通过获取的用户资料创建loginUserInfo
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        loginUserInfo.setId(githubUser.getId());
        loginUserInfo.setName(githubUser.getName());
        loginUserInfo.setAvatarUrl(githubUser.getAvatarUrl());
        loginUserInfo.setBio(githubUser.getBio());

        return loginUserInfo;
    }

    @Override
    public String getSupportedType() {
        return "gitee";
    }
}
