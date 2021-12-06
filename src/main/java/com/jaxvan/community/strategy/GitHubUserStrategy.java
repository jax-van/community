package com.jaxvan.community.strategy;

import com.jaxvan.community.dto.AccessTokenDTO;
import com.jaxvan.community.provider.GithubProvider;
import com.jaxvan.community.provider.dto.GitHubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GitHubUserStrategy implements UserStrategy {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
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
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GitHubUser githubUser = githubProvider.getUser(accessToken);

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
        return "github";
    }
}
