package com.woobros.member.hub.model.card.focus_card;

import com.woobros.member.hub.model.card.limited_affr_card.LimitedAffirmationCard;
import com.woobros.member.hub.model.card.member_card.MemberCard;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
public class FocusCard {

    /*
     * member가 집중하고 싶은 카드 저장 (즐겨찾기)
     *
     * 1. letter에 딸린 card 집중하기
     * 2. member가 집적 만든 card 집중하기
     *
     * */
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private LimitedAffirmationCard limitedAffirmationCard;

    @OneToOne
    private MemberCard memberCard;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;
}
