package com.woobros.member.hub.domain.signup.service;

import com.woobros.member.hub.model.member.MemberDto;
import com.woobros.member.hub.model.member.MemberMapper;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignUpServiceImpl implements SignUpService {

    private final MemberMapper memberMapper;

    @Transactional
    public MemberDto.Response signUp(MemberDto.Request requestDto) {
        /* 회원 가입 기능 */
        return null;
    }
}
