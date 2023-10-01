package net.lifepost.service.model.letter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LetterRepository extends JpaRepository<Letter, Long> {

    Optional<Letter> findTopByOrderByCreatedAtDesc();

    Optional<Letter> findByPostDate(LocalDate localDate);

    Optional<Letter> findTopByOrderByPostDateDesc();

    Optional<Letter> findFirstByPostDateBeforeOrderByPostDateDesc(LocalDate now);

    Page<Letter> findByIdLessThanOrderByIdDesc(Long letterId, PageRequest pageRequest);

    @Query("SELECT l FROM Letter l WHERE l.id NOT IN (SELECT ml.letter.id FROM MemberLetter ml WHERE ml.member.id = :memberId ) ORDER BY l.id DESC")
    Page<Letter> findMissLetter(Long memberId, Pageable pageable);

    Page<Letter> findAllByOrderByIdDesc(Pageable pageable);

    List<Letter> findByIdIn(List<Long> ids);

}
