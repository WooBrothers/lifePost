package net.lifepost.service.model.member_letter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.lifepost.service.common.exception.CommonException;
import net.lifepost.service.common.exception.ErrorEnum;
import net.lifepost.service.domain.card.FocusTypeEnum;
import net.lifepost.service.model.letter.Letter;
import net.lifepost.service.model.member.Member;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "letter_id"})})
public class MemberLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "letter_id", nullable = false)
    private Letter letter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FocusTypeEnum focus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    public void focusLetter(FocusTypeEnum focusType) {
        if (focusType.equals(FocusTypeEnum.ATTENTION)) {
            this.setFocus(FocusTypeEnum.ATTENTION);
        } else if (focusType.equals(FocusTypeEnum.NON)) {
            this.setFocus(FocusTypeEnum.NON);
        } else {
            throw new CommonException(ErrorEnum.LETTER_REQUEST_INVALID);
        }
    }
}
