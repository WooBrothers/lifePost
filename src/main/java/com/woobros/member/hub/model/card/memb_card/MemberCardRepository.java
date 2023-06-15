package com.woobros.member.hub.model.card.memb_card;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCardRepository extends
    JpaRepository<MemberCard, Long> {

    List<MemberCard> findByMemberLetterId(Long memberLetterId);

    Page<MemberCard> findByMemberIdOrderByCreatedAtDesc(Long memberId, PageRequest pageRequest);

    Page<MemberCard> findByMemberIdAndIdAfterOrderByCreatedAtDesc(Long memberId, Long id,
        PageRequest pageRequest);

}
