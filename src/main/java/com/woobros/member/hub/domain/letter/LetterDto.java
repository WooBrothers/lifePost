package com.woobros.member.hub.domain.letter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.woobros.member.hub.model.letter.LetterTagEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class LetterDto {

    private LetterDto() {
    }

    @Getter
    @JsonInclude(Include.NON_NULL)
    @Builder
    public static class Info {

        private final Long id;
        private final String title;
        private final String letterImage;
        private final String postStampImage;
        private final String writer;
        private final LetterTagEnum tag;
        private final LocalDateTime createdAt;
        private final LocalDateTime updateAt;
    }

    @Getter
    @Setter
    public static class PostRequest {

        @NotNull(message = "title@NotNull")
        @NotBlank(message = "title@NotBlank")
        @Size(message = "title@Size", min = 1, max = 30)
        private String title;

        @NotBlank(message = "contents@NotBlank")
        private String contents;

        @NotBlank(message = "writer@NotBlank")
        @Size(message = "writer@Size", min = 1, max = 15)
        private String writer;

        private String letterImage;
        private String postStampImage;
        private LetterTagEnum tag;
    }

    @Getter
    @JsonInclude(Include.NON_NULL)
    @Builder
    public static class ReadResponse {

        private final Long id;
        private final String title;
        private final String letterImage;
        private final String postStampImage;
        private String contents;
        private final String writer;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private final LocalDate createdDate;
        private final LetterTagEnum tag;

        public ReadResponse setContents(String contents) {
            this.contents = contents;
            return this;
        }
    }

    @Getter
    @JsonInclude(Include.NON_NULL)
    @Builder
    public static class PageResponse {

        private final Long id;
        private final String title;
        private final String letterImage;
        private final String postStampImage;
        private final String writer;
        private final LetterTagEnum tag;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private final LocalDate createdDate;
    }
}
