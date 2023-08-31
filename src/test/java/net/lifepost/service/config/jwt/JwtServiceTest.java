package net.lifepost.service.config.jwt;

import lombok.extern.slf4j.Slf4j;
import net.lifepost.service.model.member.Member;
import net.lifepost.service.model.member.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(properties = {
    "jwt.access.expiration=1",
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