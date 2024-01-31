package net.lifepost.service.model.card.memb_cust_card;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCustomCardRepository extends JpaRepository<MemberCustomCard, Long> {

    List<MemberCustomCard> findByMemberId(Long memberId);

    Optional<MemberCustomCard> findByIdAndMemberId(Long id, Long memberId);

}
