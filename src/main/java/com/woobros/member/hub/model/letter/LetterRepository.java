package com.woobros.member.hub.model.letter;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LetterRepository extends JpaRepository<Letter, Long> {

    Optional<Letter> findTopByOrderByCreatedAtDesc();

    Page<Letter> findByIdLessThanOrderByIdDesc(Long letterId, PageRequest pageRequest);

    @Query("SELECT l FROM Letter l WHERE l.id NOT IN (SELECT ml.letter.id FROM MemberLetter ml WHERE ml.member.id = :memberId ) ORDER BY l.id DESC")
    Page<Letter> findDoesNotHaveLastLetter(Long memberId, PageRequest pageable);

    @Query("SELECT l FROM Letter l WHERE l.id NOT IN (SELECT ml.letter.id FROM MemberLetter ml WHERE ml.member.id = :memberId ) and l.id < :letterId ORDER BY l.id DESC")
    Page<Letter> findDoesNotHaveLetter(Long memberId, Long letterId, PageRequest pageable);

}
