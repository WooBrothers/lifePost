package com.woobros.member.hub.model.member;

import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
    @Setter
    public static class Request {

        private static final String ENUM_NAME = "SignUpErrorEnum.";

        @NotBlank(message = Request.ENUM_NAME + "NAME_BLANK_ERROR")
        @Size(min = 1, max = 30, message = Request.ENUM_NAME + "NAME_SIZE_OUT_ERROR")
        private String name;

        @Email(message = Request.ENUM_NAME + "EMAIL_FORMAT_ERROR_ERROR")
        private String email;

        @NotBlank(message = Request.ENUM_NAME + "PASSWORD_BLANK_ERROR")
        @Size(min = 6, max = 30, message = Request.ENUM_NAME + "PASSWORD_SIZE_OUT_ERROR")
        private String password;

        @NotBlank(message = Request.ENUM_NAME + "NICK_BLANK_ERROR")
        @Size(min = 1, max = 15, message = Request.ENUM_NAME + "NICK_SIZE_OUT_ERROR")
        private String nickName;

        private String phone;

        private Boolean marketingSms;

        private Boolean marketingEmail;

        private Boolean marketingKakao;

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
}
