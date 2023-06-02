package com.woobros.member.hub.model.member_letter.affirmation_card.focus_affr_card;

import com.woobros.member.hub.model.member_letter.affirmation_card.MemberLetterAffirmationCard;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Entity
@Table(name = "FOCUS_AFFR_CARD")
public class FocusAffirmationCard {

    /*
     * 획득한 확언 카드 혹은 멤버가 생성한 카드(추후) 중 집중하고 싶은 카드 저장 (즐겨찾기)
     *
     * */
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private MemberLetterAffirmationCard memberLetterAffirmationCard;

    private String title;

    private String contents;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;
}
