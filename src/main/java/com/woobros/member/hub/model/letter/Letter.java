package com.woobros.member.hub.model.letter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String letterImage;

    private String postStampImage;

    @Lob
    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String writer;

    @Enumerated(EnumType.STRING)
    private LetterTagEnum tag;

    @Column(unique = true, nullable = false)
    @CreationTimestamp
    private LocalDate postDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public String getPreviewLetterContent() {
        return getContents().substring(0, 100) + "...";
    }

    public String getTitleByText() {
        String strPattern = "(<[a-zA-Z/]*>)*";
        Pattern pattern = Pattern.compile(strPattern);

        return pattern.matcher(this.getTitle()).replaceAll("");

    }
}
