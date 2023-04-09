package com.woobros.member.hub.domain.signup.service;

import com.woobros.member.hub.business.member.MemberDto;

public interface SignUpService {

    MemberDto.Response signUp(MemberDto.Request requestDto);
}
