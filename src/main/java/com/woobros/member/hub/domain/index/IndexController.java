package com.woobros.member.hub.domain.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {


    @GetMapping("/")
    public String index() {
        return "/index";
    }

    @GetMapping("/login/page")
    public String login() {
        return "/login";
    }

    @GetMapping("/forbidden")
    public String forbidden() {
        return "/forbidden";
    }
}
