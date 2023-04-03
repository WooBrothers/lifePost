package com.woobros.member;

import com.woobros.member.domain.logout.exception.LogoutErrorEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberApplicationTests {

    @Test
    void contextLoads() {
        String a = LogoutErrorEnum.class.getSimpleName() + ".";
        System.out.println(a);
    }

}
