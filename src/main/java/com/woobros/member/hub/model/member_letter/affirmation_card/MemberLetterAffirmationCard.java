package com.woobros.member.hub.model.member_letter.affirmation_card;

import com.woobros.member.hub.model.letter.affirmation_card.AffirmationCard;
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
@Table(name = "MEM_LTER_AFFR_CARD")
public class MemberLetterAffirmationCard {

    @Id
    @GeneratedValue
    private Long id;

    /*
     * letter 하나에 여러 card 존재 가능하다. (ex. 하나의 편지에 3개의 확언)
     * 즉 member_letter 하나에도 member_letter_affirmation_card가 여러개 존재 가능하다.
     *
     * ex) 편지 하나에 확언 카드 3개 생성 시
     * letter 1 : card 1, 2, 3
     *
     * member 1 -> letter 1 획득
     * memberLetter 1 생성
     *
     * member 1 -> card 1, 2, 3 획득
     * memberLetterAffirmationCard 1 생성 : card_id 1
     * memberLetterAffirmationCard 2 생성 : card_id 2
     * memberLetterAffirmationCard 3 생성 : card_id 3
     *
     * member 2 -> letter 1획득
     * memberLetter 2 생성
     *
     * member 2 -> card 1, 2, 3 획득
     * memberLetterAffirmationCard 4 생성 : card_id 1
     * memberLetterAffirmationCard 5 생성 : card_id 2
     * memberLetterAffirmationCard 6 생성 : card_id 3
     *
     * memberLetter : memberLetterAffirmationCard = 1: N
     * affirmationCard : memberLetterAffirmationCard = 1: N
     *
     * */
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
