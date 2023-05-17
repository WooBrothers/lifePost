package com.woobros.member.hub.model.letter;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class LetterDto {

    private LetterDto() {

    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Info {

        private final Long id;
        private final String title;
        private final String letterImage;
        private final String postStampImage;
        private final String contents;
        private final String writer;
        private final LetterTageEnum tag;
        private final LocalDateTime createdAt;
        private final LocalDateTime updateAt;
    }

    @Getter
    @Setter
    public static class Request {

        @NotBlank
        @Size(min = 1, max = 30)
        private String title;
        private String letterImage;
        private String postStampImage;
        private String contents;
        @NotBlank
        @Size(min = 1, max = 15)
        private String writer;
        private LetterTageEnum tag;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {

        private final Long id;
        private final String title;
        private final String letterImage;
        private final String postStampImage;
        private final String contents;
        private final String writer;
        private final LetterTageEnum tag;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PageResponse {

        private final Long id;
        private final String title;
        private final String letterImage;
        private final String postStampImage;
        private final String writer;
        private final LetterTageEnum tag;
        private final String createdDate;
    }
}
