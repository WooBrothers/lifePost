package com.woobros.member.hub.domain.signup;

import com.woobros.member.hub.domain.signup.service.SignUpService;
import com.woobros.member.hub.model.member.MemberDto;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sign-up")
public class SignUpController {

    private final SignUpService service;

    @PostMapping("/")
    public ResponseEntity<MemberDto.Response> signUp(
        /* 회원 가입 */
        @Valid @RequestBody MemberDto.Request requestDto) {

        MemberDto.Response responseDto = service.signUp(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}