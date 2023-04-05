package com.woobros.member.hub.domain.signup.service;

import com.woobros.member.hub.business.member.MemberDao;
import com.woobros.member.hub.domain.signup.dto.SignUpRequestDto;
import com.woobros.member.hub.domain.signup.dto.SignUpResponseDto;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SingUpServiceImpl implements SignUpService {

    private final MemberDao dao;

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        /* 회원 가입 기능 */

        // requestDto 요청 정보 유효성 검증
        // id, email, (phone) 중복 검사 -> insert 때리고 실패하면 예외처리하는게 좋을듯
        // member return

        return null;
    }
}
