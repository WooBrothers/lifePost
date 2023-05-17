package com.woobros.member.hub.domain.letter;

import com.woobros.member.hub.model.letter.LetterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/letter")
public class LetterController {

    /* beans */
    private final LetterService letterService;

    @GetMapping("/latest")
    public ResponseEntity<LetterDto.Response> getTodayLetter() {
        log.debug("postLetter controller getLetter access");

        return ResponseEntity.ok().body(letterService.getLatestLetter());
    }

    @GetMapping("/")

    @PostMapping
    public ResponseEntity<LetterDto.Response> postLetter(
        @RequestBody LetterDto.Request letterReqDto) {
        log.debug("postLetter controller postLetter access");

        return ResponseEntity.ok().body(letterService.postLetter(letterReqDto));
    }

}
