package net.lifepost.service.model.card.memb_cust_card;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCustomCardRepository extends JpaRepository<MemberCustomCard, Long> {

    Page<MemberCustomCard> findByMemberIdOrderByCreatedAtDesc(Long memberId,
        Pageable pageable);

    List<MemberCustomCard> findByMemberId(Long memberId);

    Optional<MemberCustomCard> findByIdAndMemberId(Long id, Long memberId);

}
