package com.woobros.member.hub.model.member_letter;

import com.woobros.member.hub.domain.card.FocusTypeEnum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLetterRepository extends JpaRepository<MemberLetter, Long> {

    Optional<MemberLetter> findByMemberIdAndLetterId(Long memberId, Long letterId);


    @EntityGraph(attributePaths = "letter")
    Page<MemberLetter> findByMemberIdAndFocusInOrderByCreatedAtDesc(Long memberId,
        List<FocusTypeEnum> focusTypeList, Pageable pageRequest);

    @EntityGraph(attributePaths = "letter")
    List<MemberLetter> findByMemberIdAndLetterIdInOrderByLetterIdDesc(Long memberId,
        List<Long> letterIds);
}
