package com.woobros.member.hub.domain.card;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.woobros.member.hub.model.card.CardTypeEnum;
import com.woobros.member.hub.model.letter.Letter;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
        @Size(min = 1, max = 200)
        private String contents;

        @NotBlank
        private Long letterId;

        private String tag;
    }

    @Getter
    @Setter
    public static class PostCustomRequest {

        @NotBlank
        @Size(min = 1, max = 200)
        private String contents;

        private String tag;
    }

    @Getter
    @Setter
    public static class PostFocusRequest {

        @NotBlank
        private Long cardId;

        @NotBlank
        private CardTypeEnum type;
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
        private final String contents;
        private final String tag;
        private final String createdDate;
        private final String createdAt;
        private final String updateAt;

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
    }
}
