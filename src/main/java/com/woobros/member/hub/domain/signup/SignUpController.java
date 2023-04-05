package com.woobros.member.hub.domain.signup;

import com.woobros.member.hub.domain.signup.dto.SignUpResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/sign-up")
public class SignUpController<T> {

    @PostMapping
    public ResponseEntity<SignUpResponseDto> signUp(SignUpResponseDto requestDto) {

        SignUpResponseDto responseDto = SignUpResponseDto.builder().memberId(10).build();
        return ResponseEntity.ok().body(responseDto);
    }
}
