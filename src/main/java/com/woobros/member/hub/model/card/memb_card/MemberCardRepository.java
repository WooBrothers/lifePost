package com.woobros.member.hub.model.card.memb_card;

import com.woobros.member.hub.domain.card.FocusTypeEnum;
import com.woobros.member.hub.model.card.CardTypeEnum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberCardRepository extends
    JpaRepository<MemberCard, Long> {

    List<MemberCard> findByMemberLetterId(Long memberLetterId);

    List<MemberCard> findByMemberId(Long memberId);

    Page<MemberCard> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    @Query("select mc from MemberCard mc "
        + "left outer join fetch AffirmationCard ac on mc.affirmationCard.id = ac.id "
        + "left outer join MemberCustomCard mcc on mc.memberCustomCard.id = mcc.id "
        + "where mc.member.id = :memberId and mc.type in :type and mc.focus in :focus "
        + "order by mc.createdAt desc ")
    Page<MemberCard> findMemberCardAndRelatedCardAndLetterInfos(Long memberId,
        List<CardTypeEnum> type, List<FocusTypeEnum> focus, Pageable pageable);

    Page<MemberCard> findByMemberIdAndIdLessThanOrderByCreatedAtDesc(Long memberId, Long id,
        Pageable pageable);


    Page<MemberCard> findByMemberIdAndFocusOrderByCreatedAtDesc(Long memberId,
        FocusTypeEnum focusTypeEnum, Pageable pageable);

    Optional<MemberCard> findByMemberIdAndAffirmationCardIdAndType(Long memberId,
        Long affirmationCardId,
        CardTypeEnum cardTypeEnum);

    Optional<MemberCard> findByMemberIdAndMemberCustomCardIdAndType(Long memberId,
        Long memberCustomCardId,
        CardTypeEnum cardTypeEnum);

    Optional<MemberCard> findByMemberIdAndId(Long memberId, Long memberCardId);

    @Query("SELECT mc FROM MemberCard mc "
        + "left outer join fetch AffirmationCard ac "
        + "on mc.affirmationCard.id = ac.id "
        + "left outer join fetch MemberCustomCard mcc "
        + "on mc.memberCustomCard.id = mcc.id "
        + "where mc.focus='ATTENTION' "
        + "and mc.id < :memberCardId "
        + "and mc.member.id = :memberId "
        + "order by mc.id desc")
    List<MemberCard> findByMemberFocusCards(Long memberCardId, Long memberId, Pageable pageable);

    @EntityGraph(attributePaths = {"affirmationCard", "memberCustomCard"})
    Page<MemberCard> findByIdIsLessThanAndMemberIdAndFocusOrderByIdDesc(Long memberCardId,
        Long memberId, FocusTypeEnum focusTypeEnum, Pageable pageable);
}
