package com.woobros.member.hub.business.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    /*
     * 유저의 권한 설정을 하는 이넘 클래스
     * 스프링 시큐리티 사용시 해당 Role 이라는 이름의 클래스는 필수다.
     * 그리고 권한 값이 키 "ROLE_" 로 시작해야 한다.
     */
    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;

}
