package com.woobros.member.hub.model.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class MemberDto {

    private MemberDto() {
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Info {

        private final Long memberId;
        private final String name;
        private final String email;
        private final String phone;
        private final boolean marketingSms;
        private final boolean marketingEmail;
        private final boolean marketingKakao;
        private final String password;
        private final String nickName;
        private final LocalDateTime createdAt;
        private final LocalDateTime updateAt;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {

        private final Long memberId;
        private final String name;
        private final String email;
        private final String phone;
        private final boolean marketingSms;
        private final boolean marketingEmail;
        private final boolean marketingKakao;
        private final String password;
        private final String nickName;
        private final LocalDateTime createdAt;
        private final LocalDateTime updateAt;
    }

    @Getter
    @Builder
    public static class InfoResponse {

        private final String email;
        private final String socialType;
        private final String stampCount;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime signInDate;

        public InfoResponse setSignInDate(LocalDateTime signInDate) {
            this.signInDate = signInDate;
            return this;
        }
    }
}
