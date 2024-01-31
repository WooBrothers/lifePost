package net.lifepost.service.model.letter;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Long> {

    Page<Letter> findByIdLessThanOrderByIdDesc(Long letterId, PageRequest pageRequest);

    Page<Letter> findByPostDateBeforeOrderByPostDateDesc(LocalDate postDate, Pageable pageable);

    List<Letter> findByIdIn(List<Long> ids);
}
