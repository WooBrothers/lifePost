package net.lifepost.service.domain.letter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.lifepost.service.domain.card.FocusTypeEnum;
import net.lifepost.service.model.letter.LetterTagEnum;

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
            this.contents = LetterDto.setLimitedContents(content);
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
            return result.trim().replace("_", " ");
        }

        public ReadResponse setLimitedContentToLogoutMember() {
            this.contents = LetterDto.setLetterContentToLogoutMember(this.contents);
            return this;
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
            this.content = LetterDto.setLimitedContents(content);
            return this;
        }

        public PageResponse setFocusType(FocusTypeEnum focusType) {
            this.focusType = focusType;
            return this;
        }
    }

    public static String setLimitedContents(String content) {
        String imgTagLessContent = removeImageTags(content);
        StringBuilder sb = new StringBuilder(imgTagLessContent);

        if (sb.length() < 200) {
            sb.setLength(sb.length() / 2);
        } else {
            sb.setLength(200);
        }
        sb.append("...");

        return sb.toString();
    }

    public static String removeImageTags(String html) {
        // 이미지 태그를 제거하는 정규식 패턴
        String regex = "<img[^>]*>";

        // 정규식 패턴과 일치하는 부분을 찾아 제거
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);

        return matcher.replaceAll("");
    }

    public static String setLetterContentToLogoutMember(String letterContent) {
        // 이미지 태그를 제거하는 정규식 패턴
        String regex = "<img[^>]*>";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(letterContent);

        List<Map<String, Object>> imgTagList = new ArrayList<>();
        // 이미지 태그 내용 및 인덱스 추가
        for (int i = 1; i <= matcher.groupCount(); i++) {
            Map<String, Object> tagMap = new HashMap<>();

            tagMap.put("imgTag", matcher.group(i));
            tagMap.put("stIdx", matcher.start(i));

            imgTagList.add(tagMap);
        }

        // 이미지 태그 제거
        String imgTagLessContent = matcher.replaceAll("");
        StringBuilder sb = new StringBuilder(imgTagLessContent);

        // 컨텐츠 제한
        sb.setLength(imgTagLessContent.length() / 2);

        // 이미지 태그 위치가 제한된 컨텐츠의 총 길이보다 작다면 이미지 태그 추가
        imgTagList.forEach(imgTagMap -> {
            int stIdx = (int) imgTagMap.get("stIdx");
            if (sb.length() > stIdx) {
                sb.insert(stIdx, imgTagMap.get("imgTag"));
            }
        });

        sb.append("...");

        return sb.toString();
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
