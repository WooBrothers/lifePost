package com.woobros.member.hub.model.card.limited_affr_card;

import com.woobros.member.hub.model.card.affirmation_card.AffirmationCard;
import com.woobros.member.hub.model.member_letter.MemberLetter;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Entity
@Table(name = "limited_affr_card")
public class LimitedAffirmationCard {

    /*
     * letter 하나에 여러 card 존재 가능하다. (ex. 하나의 편지에 3개의 확언)
     * 즉 member_letter 하나에도 limited_affirmation_card가 여러개 존재 가능하다.
     *
     * ex) 편지 하나에 확언 카드 3개 생성 시
     * letter 1 : card 1, 2, 3
     *
     * member 1 -> letter 1 획득
     * memberLetter 1 생성
     *
     * member 1 -> card 1, 2, 3 획득
     * limitedAffirmationCard 1 생성 : card_id 1
     * limitedAffirmationCard 2 생성 : card_id 2
     * limitedAffirmationCard 3 생성 : card_id 3
     *
     * member 2 -> letter 1획득
     * memberLetter 2 생성
     *
     * member 2 -> card 1, 2, 3 획득
     * limitedAffirmationCard 4 생성 : card_id 1
     * limitedAffirmationCard 5 생성 : card_id 2
     * limitedAffirmationCard 6 생성 : card_id 3
     *
     * memberLetter : limitedAffirmationCard = 1: N
     * affirmationCard : limitedAffirmationCard = 1: N
     *
     * */

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    private MemberLetter memberLetter;

    @ManyToOne
    @JoinColumn
    private AffirmationCard affirmationCard;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;
}
