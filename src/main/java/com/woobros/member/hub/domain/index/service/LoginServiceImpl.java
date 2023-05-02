package com.woobros.member.hub.domain.index.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {


    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public String signIn(String code) {
        /* 로그인 */

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity request = new HttpEntity(headers);

        return null;
    }
}

