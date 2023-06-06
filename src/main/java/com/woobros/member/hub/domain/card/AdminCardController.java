package com.woobros.member.hub.domain.card;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth/admin/api/v1/card/affirmation")
public class AdminCardController {

    private final CardService cardService;


}
