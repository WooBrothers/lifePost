package com.woobros.member.hub.config.jwt;

import com.woobros.member.hub.model.member.Member;
import com.woobros.member.hub.model.member.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(properties = {
    "jwt.access.expiration=999999999",
    "jwt.refresh.expiration=999999999",
})
class JwtServiceTest {

    @Autowired
    JwtService jwtService;

    @Test
    void TestCreateAccessToken() {

        Member member = Member.builder()
            .email("gojalani456@gmail.com")
            .role(Role.GUEST)
            .build();

        System.out.println("\n" + "[create Long term access token]");
        System.out.println(jwtService.createAccessToken(member));
        System.out.println("\n" + "[create Long term access token]");
        System.out.println(jwtService.createRefreshToken());
    }
}