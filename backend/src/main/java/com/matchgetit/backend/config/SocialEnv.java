package com.matchgetit.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SocialEnv {
    @Value("${naver.tokenUrl}")
    private String naverTokenUrl;
    @Value("${naver.userInfoUrl}")
    private String naverInfoUrl;
    @Value("${naver.clientId}")
    private String naverClientId;
    @Value("${naver.secret}")
    private String naverSecret;

    @Value("${google.token}")
    private String googleTokenUrl;
    @Value("${google.info}")
    private String googleInfo;
    @Value("${google.cliendId}")
    private String googleClientId;
    @Value("${google.secret}")
    private String googleSecret;

    @Value("${kakao.tokenUrl}")
    private String kakaoTokenUrl;
    @Value("${kakao.info}")
    private String kakaoInfo;
    @Value("${kakao.clientId}")
    private String kakaoClientId;
    @Value("${kakao.secret}")
    private String kakaoSecret;




}
