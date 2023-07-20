package com.matchgetit.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchgetit.backend.config.SocialEnv;
import com.matchgetit.backend.loginAPI.NaverUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class NaverService {
    private final SocialEnv socialEnv;
    private final WebClient webClient;

    public NaverUser getNaverUserInfo(String code) {
        String accessToken = getAccessToken(code);
        if (accessToken != null) {
            return requestUserInfo(accessToken);
        }
        return null;
    }

    private String getAccessToken(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", socialEnv.getNaverClientId());
        body.add("client_secret", socialEnv.getNaverSecret());
        body.add("code", code);

        return webClient.post()
                .uri(socialEnv.getNaverTokenUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .bodyToMono(String.class)
                .map(responseJson -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = objectMapper.readTree(responseJson);
                        return rootNode.path("access_token").asText();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .block(); // block to get the result synchronously (not recommended in a reactive environment)
    }

    private NaverUser requestUserInfo(String accessToken) {
        return webClient.get()
                .uri(socialEnv.getNaverInfoUrl())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .map(responseJson -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = objectMapper.readTree(responseJson);
                        JsonNode responseNode = rootNode.path("response");

                        if (!responseNode.isMissingNode()) {
                            return objectMapper.treeToValue(responseNode, NaverUser.class);
                        }
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .block(); // block to get the result synchronously (not recommended in a reactive environment)
    }
}
