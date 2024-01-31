package net.lifepost.service.model.card.memb_card;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.lifepost.service.domain.card.FocusTypeEnum;
import net.lifepost.service.model.card.CardTypeEnum;
import net.lifepost.service.model.card.affr_card.AffirmationCard;
import net.lifepost.service.model.card.memb_cust_card.MemberCustomCard;
import net.lifepost.service.model.member.Member;
import net.lifepost.service.model.member_letter.MemberLetter;
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
public class MemberCard {

    /**
     * 멤버가 편지를 읽음으로 획득한 카드
     * <p>
     * letter 하나에 여러 card 존재 가능하다. (ex. 하나의 편지에 3개의 확언) 즉 member_letter 하나에도
     * limited_affirmation_card가 여러개 존재 가능하다.
     * <p>
     * ex) 편지 하나에 확언 카드 3개 생성 시 letter 1 : card 1, 2, 3
     * <p>
     * member 1 -> letter 1 획득 memberLetter 1 생성
     * <p>
     * member 1 -> card 1, 2, 3 획득 limitedAffirmationCard 1 생성 : card_id 1 limitedAffirmationCard 2
     * 생성 : card_id 2 limitedAffirmationCard 3 생성 : card_id 3
     * <p>
     * member 2 -> letter 1획득 memberLetter 2 생성
     * <p>
     * member 2 -> card 1, 2, 3 획득 limitedAffirmationCard 4 생성 : card_id 1 limitedAffirmationCard 5
     * 생성 : card_id 2 limitedAffirmationCard 6 생성 : card_id 3
     * <p>
     * memberLetter : limitedAffirmationCard = 1: N affirmationCard : limitedAffirmationCard = 1: N
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardTypeEnum type;

    @ManyToOne
    @JoinColumn
    private MemberLetter memberLetter;

    @ManyToOne
    @JoinColumn
    private AffirmationCard affirmationCard;

    @OneToOne
    @JoinColumn
    private MemberCustomCard memberCustomCard;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FocusTypeEnum focus = FocusTypeEnum.NON;

    @Column(columnDefinition = "int default 0")
    private Long writeCount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

}
