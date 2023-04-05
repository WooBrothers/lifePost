package com.woobros.member.hub.domain.signup.service;

import com.woobros.member.hub.domain.signup.dto.SignUpRequestDto;
import com.woobros.member.hub.domain.signup.dto.SignUpResponseDto;

public interface SignUpService {

    SignUpResponseDto signUp(SignUpRequestDto requestDto);
}
