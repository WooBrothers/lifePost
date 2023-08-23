package com.woobros.member.hub.model.stamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampRepository extends JpaRepository<Stamp, Long> {

    Optional<Stamp> findByMemberIdAndActionAndCreatedAtBetween(Long memberId, Integer action,
        LocalDateTime startDate, LocalDateTime endDate
    );

    List<Stamp> findByMemberId(Long memberId);

}
