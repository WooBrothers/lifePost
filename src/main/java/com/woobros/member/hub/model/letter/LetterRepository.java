package com.woobros.member.hub.model.letter;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Long> {

    Optional<Letter> findTopByOrderByCreatedAtDesc();
}
