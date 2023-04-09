package com.woobros.member.hub.business.member;

import com.woobros.member.hub.business.exception.BusinessException;

public interface MemberDao {

    Member selectById(Long memberId) throws BusinessException;

    Member createMember(Member member);

    boolean checkUserExistence(Member member);
}
