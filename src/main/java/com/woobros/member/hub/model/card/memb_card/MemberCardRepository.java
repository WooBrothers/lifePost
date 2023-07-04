package com.woobros.member.hub.model.card.memb_card;

import com.woobros.member.hub.domain.card.FocusTypeEnum;
import com.woobros.member.hub.model.card.CardTypeEnum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCardRepository extends
    JpaRepository<MemberCard, Long> {

    List<MemberCard> findByMemberLetterId(Long memberLetterId);

    List<MemberCard> findByMemberId(Long memberId);

    Page<MemberCard> findByMemberIdOrderByCreatedAtDesc(Long memberId, PageRequest pageRequest);

    Page<MemberCard> findByMemberIdAndIdLessThanOrderByCreatedAtDesc(Long memberId, Long id,
        PageRequest pageRequest);

    Page<MemberCard> findByMemberIdAndFocusOrderByCreatedAtDesc(Long memberId,
        FocusTypeEnum focusTypeEnum, PageRequest pageRequest);

    Page<MemberCard> findByMemberIdAndFocusAndIdLessThanOrderByCreatedAtDesc(Long memberId,
        FocusTypeEnum focus, Long id, Pageable pageable);

    Optional<MemberCard> findByMemberIdAndAffirmationCardIdAndType(Long memberId,
        Long affirmationCardId,
        CardTypeEnum cardTypeEnum);

    Optional<MemberCard> findByMemberIdAndMemberCustomCardIdAndType(Long memberId,
        Long memberCustomCardId,
        CardTypeEnum cardTypeEnum);

}
