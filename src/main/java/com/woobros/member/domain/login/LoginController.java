package com.woobros.member.domain.login;

import com.woobros.member.entity.member.Member;
import org.springframework.web.bind.annotation.RestController;

@RestController("/login")
public class LoginController {


    public String getMember() {

        Member member = new Member();
        member.setName("김경우");
        member.setEmailId("wookim789@gmail.com");
        member.setPassword("1234");
        member.setNickName("wookim");
        member.setPhoneNumber("01000000000");

        return "";
    }
}
