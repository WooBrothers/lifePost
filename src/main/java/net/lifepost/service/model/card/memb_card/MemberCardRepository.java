package net.lifepost.service.model.card.memb_card;

import java.util.List;
import java.util.Optional;
import net.lifepost.service.domain.card.FocusTypeEnum;
import net.lifepost.service.model.card.CardTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCardRepository extends
    JpaRepository<MemberCard, Long> {

    List<MemberCard> findByMemberLetterId(Long memberLetterId);

    List<MemberCard> findByMemberId(Long memberId);

    Page<MemberCard> findByMemberIdAndIdLessThanOrderByCreatedAtDesc(Long memberId, Long id,
        Pageable pageable);

    Optional<MemberCard> findByMemberIdAndAffirmationCardIdAndType(Long memberId,
        Long affirmationCardId,
        CardTypeEnum cardTypeEnum);

    Optional<MemberCard> findByMemberIdAndMemberCustomCardIdAndType(Long memberId,
        Long memberCustomCardId,
        CardTypeEnum cardTypeEnum);

    Optional<MemberCard> findByMemberIdAndId(Long memberId, Long memberCardId);

    @EntityGraph(attributePaths = {"affirmationCard", "memberCustomCard"})
    Page<MemberCard> findByMemberIdAndFocusInAndTypeInOrderByIdDesc(
        Long memberId, List<FocusTypeEnum> focusTypeEnums, List<CardTypeEnum> cardTypeEnums,
        Pageable pageable);

    @EntityGraph(attributePaths = {"affirmationCard", "memberCustomCard"})
    Page<MemberCard> findByIdIsLessThanAndMemberIdAndFocusInAndTypeInOrderByIdDesc(
        Long memberCardId,
        Long memberId, List<FocusTypeEnum> focusTypeEnums, List<CardTypeEnum> cardTypeEnums,
        Pageable pageable);
}
