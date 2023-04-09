package com.woobros.member.hub.business.member;

import com.woobros.member.hub.business.exception.BusinessErrorEnum;
import com.woobros.member.hub.business.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberDaoImpl implements MemberDao {

    private final MemberRepository repo;

    public Member selectById(Long memberId) throws BusinessException {
        return repo.findById(memberId).orElseThrow(
            () -> new BusinessException(BusinessErrorEnum.NOT_FOUND.setFieldName("memberId"))
        );
    }

    public Member createMember(Member member) {
        return repo.save(member);
    }

    public boolean checkUserExistence(Member member) {
        return repo.findByEmail(member.getEmail()).isEmpty();
    }
}
