package net.lifepost.service.model.member_letter;

import java.util.List;
import java.util.Optional;
import net.lifepost.service.domain.card.FocusTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLetterRepository extends JpaRepository<MemberLetter, Long> {

    Optional<MemberLetter> findByMemberIdAndLetterId(Long memberId, Long letterId);

    List<MemberLetter> findByMemberId(Long memberId);

    @EntityGraph(attributePaths = "letter")
    Page<MemberLetter> findByMemberIdAndFocusInOrderByLetterIdDesc(Long memberId,
        List<FocusTypeEnum> focusTypeList, Pageable pageRequest);

    @EntityGraph(attributePaths = "letter")
    Page<MemberLetter> findByMemberIdAndFocusInAndLetterIdLessThanOrderByLetterIdDesc(Long memberId,
        List<FocusTypeEnum> focusTypeList, Long letterId, Pageable pageRequest);

    @EntityGraph(attributePaths = "letter")
    List<MemberLetter> findByMemberIdAndLetterIdInOrderByLetterIdDesc(Long memberId,
        List<Long> letterIds);
}
