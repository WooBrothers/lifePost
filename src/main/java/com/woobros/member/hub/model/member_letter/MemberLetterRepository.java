package com.woobros.member.hub.model.member_letter;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLetterRepository extends JpaRepository<MemberLetter, Long> {

    Optional<MemberLetter> findByMemberIdAndLetterId(Long memberId, Long letterId);
}
