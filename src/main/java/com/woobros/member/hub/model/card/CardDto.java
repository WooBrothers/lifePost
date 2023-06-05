package com.woobros.member.hub.model.card;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.woobros.member.hub.model.letter.Letter;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class CardDto {

    @Getter
    @JsonInclude(Include.NON_NULL)
    @Builder
    public static class Info {

        private final Long id;
        private final Letter letter;
        private final String title;
        private final String tag;
        private final CardTypeEnum type;
        private final LocalDateTime createdAt;
        private final LocalDateTime updateAt;
    }

    @Getter
    @Setter
    public static class PostRequest {

        @NotBlank
        @Size(min = 1, max = 200)
        private String contents;

        @NotBlank
        private Long letterId;

        @NotBlank
        private String title;

        private String tag;
    }

    @Getter
    @Setter
    public static class PostFocusRequest {

        @NotBlank
        private Long CardId;

        @NotBlank
        private Long letterId;

        @NotBlank
        private CardTypeEnum type;
    }

    @Getter
    @JsonInclude(Include.NON_NULL)
    @Builder
    public static class ReadResponse {

        private final Long id;
        private final Letter letter;
        private final String title;
        private final String contents;
        private final CardTypeEnum type;
        private final String tag;
        private final String createdAt;
        private final String updateAt;
    }

    @Getter
    @JsonInclude(Include.NON_NULL)
    @Builder
    public static class PageResponse {

        private final Long id;
        private final Letter letter;
        private final String title;
        private final String tag;
        private final CardTypeEnum type;
        private final String createdDate;
        private final String createdAt;
        private final String updateAt;
    }
}
