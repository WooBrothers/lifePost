package com.woobros.member.hub.domain.letter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/v1/latter")
public class LetterController {

    @GetMapping
    public ResponseEntity<String> getLetter() {
        log.info("getLetter");
        return ResponseEntity.ok().body("can access");
    }
}
