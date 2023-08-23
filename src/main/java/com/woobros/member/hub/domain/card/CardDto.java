package com.woobros.member.hub.domain.card;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.woobros.member.hub.model.card.CardTypeEnum;
import com.woobros.member.hub.model.letter.Letter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class CardDto {

    private CardDto() {
    }

    @Getter
    @JsonInclude(Include.NON_NULL)
    @Builder
    public static class Info {

        private final Long id;
        private final Letter letter;
        private final String tag;
        private final CardTypeEnum type;
        private final LocalDateTime createdAt;
        private final LocalDateTime updateAt;
    }

    @Getter
    @Setter
    public static class PostRequest {

        @NotBlank
        @Size(min = 1, max = 200, message = "contents@Size")
        private String contents;

        @NotBlank
        private Long letterId;

        private String tag;
    }

    @Getter
    @Setter
    public static class PostCustomRequest {

        private Long cardId;

        @NotNull(message = "contents@NotNull")
        @NotBlank(message = "contents@NotBlank")
        private String contents;

        private String tag;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeleteCustomRequest {

        @NotNull(message = "memberCustomCardId@NotNull")
        private Long cardId;
        private CardTypeEnum type;
    }

    @Getter
    @Setter
    public static class PostFocusRequest {

        @NotNull(message = "cardId@NotNull")
        private Long cardId;

        @NotNull(message = "type@NotNull")
        private CardTypeEnum type;
    }

    @Getter
    @Setter
    public static class PostWriteRequest {

        @NotBlank
        private Long memberCardId;

        private Long count = 10L;
    }

    @Getter
    @JsonInclude(Include.NON_NULL)
    @Builder
    public static class ReadResponse {

        private final Long id;
        private final String contents;
        private CardTypeEnum type;
        private final String tag;
        private final String createdAt;
        private final String updateAt;

        public ReadResponse setType(CardTypeEnum type) {
            this.type = type;
            return this;
        }
    }

    @Getter
    @JsonInclude(Include.NON_NULL)
    @Builder
    public static class PageResponse {

        private Long memberCardId;
        private Long cardId;
        private CardTypeEnum type;
        private FocusTypeEnum focus;
        private String letterTitle;
        private Long letterId;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate postDate;
        private final String contents;
        private final String tag;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private final LocalDateTime createdAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private final LocalDateTime updateAt;

        public PageResponse setType(CardTypeEnum cardTypeEnum) {
            this.type = cardTypeEnum;
            return this;
        }

        public PageResponse setFocus(FocusTypeEnum focus) {
            this.focus = focus;
            return this;
        }

        public PageResponse setMemberCardId(Long memberCardId) {
            this.memberCardId = memberCardId;
            return this;
        }

        public PageResponse setCardId(Long cardId) {
            this.cardId = cardId;
            return this;
        }

        public PageResponse setLetterId(Long letterId) {
            this.letterId = letterId;
            return this;
        }

        public PageResponse setLetterTitle(String letterTitle) {
            this.letterTitle = letterTitle;
            return this;
        }

        public PageResponse setPostDate(LocalDate postDate) {
            this.postDate = postDate;
            return this;
        }
    }
}
