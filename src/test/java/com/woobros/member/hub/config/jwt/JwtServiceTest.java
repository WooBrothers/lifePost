package com.woobros.member.hub.config.jwt;

import com.woobros.member.hub.model.member.Member;
import com.woobros.member.hub.model.member.MemberRepository;
import com.woobros.member.hub.model.member.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
class JwtServiceTest {

    @MockBean
    private MemberRepository userRepository;

    @Test
    void TestCreateAccessToken() {
        JwtService jwtService = new JwtService(userRepository);

        // TODO @value 값 세팅한 ReflectionTestUtils 정보 수정 필요, secretKey 도 해당 파일에 노출 됐으니 꼭 수정해야함
        ReflectionTestUtils
            .setField(jwtService, "accessTokenExpirationPeriod",
                3600000L);
        ReflectionTestUtils
            .setField(jwtService, "secretKey",
                "replace this"); // Set the desired value

        Member guest = Member.builder()
            .email("gojalani456@gmail.com")
            .role(Role.GUEST)
            .build();
        Member user = Member.builder()
            .email("gojalani456@gmail.com")
            .role(Role.USER)
            .build();
        Member admin = Member.builder()
            .email("gojalani456@gmail.com")
            .role(Role.ADMIN)
            .build();

        System.out.println("\n" + "[guest]");
        System.out.println(jwtService.createAccessToken(guest));
        System.out.println("\n" + "[user]");
        System.out.println(jwtService.createAccessToken(user));
        System.out.println("\n" + "[admin]");
        System.out.println(jwtService.createAccessToken(admin));
    }
}