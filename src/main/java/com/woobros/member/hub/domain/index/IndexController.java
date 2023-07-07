package com.woobros.member.hub.domain.index;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class IndexController {


    @GetMapping("/")
    public String index() {
        return "/index";
    }

    @GetMapping("/login/page")
    public String login() {
        return "/login";
    }

    @GetMapping("/forbidden")
    public String forbidden() {
        return "/forbidden";
    }

    @GetMapping("/auth/new/token")
    public ResponseEntity<Map<String, String>> getVerifiedToken() {
        /**
         * cookie에 토큰이 있는 유저일 경우
         * access Token가 만료됬는지 확인을 위한 메소드
         *
         * 실제 확인 로직은 security를 이용한다.QA
         *
         * [플로우]
         * 1. 멤버 -> lifePost url 접근
         * 2. 브라우저 쿠키 확인 -> accessToken 여부 확인
         * 3. 있을 경우 -> 해당 api call
         * 4. spring security 토큰 확인
         * 5. 만료 되었을 경우 401
         * 6. 아닐 경우 문제 없음
         * 7. 만료 되었을 경우 refresh token을 이용해 accessToken 최신화
         * 8. 이후 api call 시 401 문제 안나도록 세팅
         * */
        log.debug("getVerifiedToken called.");
        Map<String, String> result = new HashMap<>();
        result.put("result", "ok");
        return ResponseEntity.ok(result);
    }
}
