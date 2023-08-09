package com.woobros.member.hub.domain.letter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.woobros.member.hub.domain.card.FocusTypeEnum;
import com.woobros.member.hub.model.letter.LetterTagEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
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
        private final LocalDate postDate;
        private final LetterTagEnum tag;
        private FocusTypeEnum focusType;

        public ReadResponse setContents(String contents) {
            this.contents = contents;
            return this;
        }

        public ReadResponse setLimitedContents(String content) {
            StringBuilder sb = new StringBuilder(content);
            if (sb.length() < 150) {
                sb.setLength(sb.length() / 2);
            } else {
                sb.setLength(150);
            }
            sb.append("...");

            this.contents = sb.toString();
            return this;
        }

        public ReadResponse setFocusType(FocusTypeEnum focusType) {
            this.focusType = focusType;
            return this;
        }

        public String getContentWithOutTag() {
            String tagPattern = "(<[^>]*>)|(&nbsp;)";
            Pattern pattern = Pattern.compile(tagPattern);
            String result = pattern.matcher(this.getContents()).replaceAll("_");
            return result.trim().replaceAll("_", " ");
        }

    }

    @Getter
    @JsonInclude(Include.NON_NULL)
    @Builder
    public static class PageResponse {

        private final Long id;
        private Long memberLetterId;
        private final String title;
        private final String letterImage;
        private final String postStampImage;
        private final String writer;
        private String content;
        private final LetterTagEnum tag;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private final LocalDate postDate;
        private FocusTypeEnum focusType;

        public PageResponse setMemberLetterId(Long memberLetterId) {
            this.memberLetterId = memberLetterId;
            return this;
        }

        public PageResponse setContent(String content) {
            this.content = content;
            return this;
        }

        public PageResponse setLimitedContent(String content) {
            StringBuilder sb = new StringBuilder(content);
            if (sb.length() < 150) {
                sb.setLength(sb.length() / 2);
            } else {
                sb.setLength(150);
            }
            sb.append("...");

            this.content = sb.toString();
            return this;
        }

        public PageResponse setFocusType(FocusTypeEnum focusType) {
            this.focusType = focusType;
            return this;
        }
    }

    @Getter
    @Setter
    public static class PostFocusRequest {

        @NotNull
        private Long letterId;

        @NotNull
        private Long memberLetterId;

        @NotNull
        private FocusTypeEnum focusType;
    }
}
