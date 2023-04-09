package com.woobros.member.hub.domain.signup.service;

import com.woobros.member.hub.business.member.Member;
import com.woobros.member.hub.business.member.MemberDao;
import com.woobros.member.hub.business.member.MemberDto;
import com.woobros.member.hub.business.member.MemberMapper;
import com.woobros.member.hub.domain.signup.exception.SignUpException;
import com.woobros.member.hub.domain.signup.exception.SignUpMemberErrorEnum;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignUpServiceImpl implements SignUpService {

    private final MemberDao dao;
    private final MemberMapper memberMapper;

    @Transactional
    public MemberDto.Response signUp(MemberDto.Request requestDto) {
        /* 회원 가입 기능 */

        Member member = memberMapper.toEntity(requestDto);

        if (dao.checkUserExistence(member)) {
            member = dao.createMember(member);
        } else {
            throw new SignUpException(SignUpMemberErrorEnum.EMAIL_DUPLICATED_ERROR);
        }

        return memberMapper.toResponse(member);
    }
}
