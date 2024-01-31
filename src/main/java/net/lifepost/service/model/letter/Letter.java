package net.lifepost.service.model.letter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
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
    @Column(nullable = false, columnDefinition = "CLOB")
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

    public String getTitleByText() {
        String strPattern = "(<[a-zA-Z/]*>)*";
        Pattern pattern = Pattern.compile(strPattern);

        return pattern.matcher(this.getTitle()).replaceAll("");

    }
}
