package com.woobros.member.hub.model.card.affirmation_card;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffirmationCardRepository extends JpaRepository<AffirmationCard, Long> {

    List<AffirmationCard> findByLetterId(Long letterId);

}
