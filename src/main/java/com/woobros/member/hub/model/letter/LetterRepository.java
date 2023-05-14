package com.woobros.member.hub.model.letter;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Long> {

    Letter findTopByOrderByCreatedAtDesc();
}
