package com.woobros.member.hub.model.member_letter;

import com.woobros.member.hub.common.exception.CommonException;
import com.woobros.member.hub.common.exception.ErrorEnum;
import com.woobros.member.hub.domain.card.FocusTypeEnum;
import com.woobros.member.hub.model.letter.Letter;
import com.woobros.member.hub.model.member.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
