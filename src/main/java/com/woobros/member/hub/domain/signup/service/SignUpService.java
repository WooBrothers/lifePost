package com.woobros.member.hub.domain.signup.service;

import com.woobros.member.hub.model.member.MemberDto;

public interface SignUpService {

    MemberDto.Response signUp(MemberDto.Request requestDto);
}
