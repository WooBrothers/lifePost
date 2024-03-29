package net.lifepost.service.domain.card;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.lifepost.service.model.card.CardTypeEnum;
import net.lifepost.service.model.letter.Letter;

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
        private final String title;
        private final String contents;
        private final String cardImg;
        private final CardTypeEnum type;
        private final LocalDateTime createdAt;
        private final LocalDateTime updateAt;
    }

    @Getter
    @Setter
    public static class PostRequest {

        @NotBlank(message = "title@NotBlank")
        @Size(min = 1, max = 100, message = "title@Size")
        private String title;

        @NotBlank(message = "contents@NotBlank")
        @Size(min = 1, max = 200, message = "contents@Size")
        private String contents;

        private String cardImg;

        @NotBlank(message = "letterId@NotBlank")
        private Long letterId;

        private String tag;
    }

    @Getter
    @Setter
    public static class PostCustomRequest {

        private Long cardId;

        @NotBlank(message = "title@NotBlank")
        @Size(min = 1, max = 100, message = "title@Size")
        private String title;

        @NotBlank(message = "contents@NotBlank")
        @Size(min = 1, max = 200, message = "contents@Size")
        private String contents;

        private String cardImg;

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

        @NotNull(message = "memberCardId@NotNull")
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
        private String cardTitle;
        private String cardImg;
        private Long writeCount;

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

        public PageResponse setCardTitle(String cardTitle) {
            this.cardTitle = cardTitle;
            return this;
        }

        public PageResponse setCardImg(String cardImg) {
            this.cardImg = cardImg;
            return this;
        }

        public PageResponse setWriteCount(Long writeCount) {
            this.writeCount = writeCount;
            return this;
        }
    }
}
