package com.woobros.member.hub.domain.letter;

import com.woobros.member.hub.model.letter.LetterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/open/latest")
    public ResponseEntity<LetterDto.ReadResponse> getTodayLetter() {

        log.debug("/api/v1/letter/latest getTodayLetter access");
        return ResponseEntity.ok().body(letterService.getLatestLetter());
    }

    @GetMapping("/open/page/{lastLetterId}/{size}")
    public Page<LetterDto.PageResponse> getLettersPage(
        @PathVariable Long lastLetterId,
        @PathVariable int size) {

        log.debug(
            "/api/v1/letter/page/" + lastLetterId + "/" + size
                + " getLettersPage access");
        return letterService.getLettersPage(lastLetterId, size);
    }

    @PostMapping("/admin")
    public ResponseEntity<LetterDto.ReadResponse> postLetter(
        @RequestBody LetterDto.PostRequest letterReqDto) {

        log.debug("/api/v1/letter postLetter access");
        return ResponseEntity.ok().body(letterService.postLetter(letterReqDto));
    }
}
