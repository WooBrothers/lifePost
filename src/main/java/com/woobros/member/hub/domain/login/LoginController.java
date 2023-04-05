package com.woobros.member.hub.domain.login;

import com.woobros.member.hub.business.member.Member;
import org.springframework.web.bind.annotation.RestController;

@RestController("/login")
public class LoginController {


    public String getMember() {

        Member member = new Member();

        return "";
    }
}
