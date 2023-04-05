package com.woobros.member.hub.business.member;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberDaoImpl implements MemberDao {

    private final MemberRepository repo;

    public Member getMemberById(Long memberId) throws NotFoundException {
        return repo.findById(memberId).orElseThrow(
            () -> new NotFoundException("")
        );
    }

}
