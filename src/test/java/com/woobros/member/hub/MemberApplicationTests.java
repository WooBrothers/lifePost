package com.woobros.member.hub;

import com.woobros.member.hub.domain.logout.exception.LogoutMemberErrorEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberApplicationTests {

    @Test
    void contextLoads() {
        String a = LogoutMemberErrorEnum.class.getSimpleName() + ".";
        System.out.println(a);
    }
}
