package com.woobros.member.hub.model.card.memb_cust_card;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCustomCardRepository extends JpaRepository<MemberCustomCard, Long> {

    Page<MemberCustomCard> findByMemberIdOrderByCreatedAtDesc(Long memberId,
        PageRequest pageRequest);

    Page<MemberCustomCard> findByMemberIdAndIdLessThanOrderByCreatedAtDesc(Long memberId, Long id,
        PageRequest pageRequest);
}