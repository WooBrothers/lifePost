package com.woobros.member.hub.domain.letter;

import com.woobros.member.hub.model.letter.LetterDto;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    private static final String DEFAULT_URL = "/api/v1/letter";

    @GetMapping("/open/latest")
    public ResponseEntity<LetterDto.ReadResponse> getTodayLetter() {

        log.debug(DEFAULT_URL + "latest getTodayLetter access");
        return ResponseEntity.ok().body(letterService.getLatestLetter());
    }

    @GetMapping("/auth/latest")
    public ResponseEntity<LetterDto.ReadResponse> getTodayLetterAuth() {

        log.debug(DEFAULT_URL + "latest getTodayLetterAuth access");
        return ResponseEntity.ok().body(letterService.getLatestLetter());
    }

    @GetMapping("/admin/latest")
    public ResponseEntity<LetterDto.ReadResponse> getTodayLetterAdmin() {

        log.debug(DEFAULT_URL + "latest getTodayLetterAdmin access");
        return ResponseEntity.ok().body(letterService.getLatestLetter());
    }

    @GetMapping("/open/page/{lastLetterId}/{size}")
    public Page<LetterDto.PageResponse> getLettersPage(
        @PathVariable Long lastLetterId,
        @PathVariable int size) {

        log.debug(
            DEFAULT_URL + "page/" + lastLetterId + "/" + size
                + " getLettersPage access");
        return letterService.getLettersPage(lastLetterId, size);
    }

    @GetMapping("/auth/{letterId}")
    public ResponseEntity<LetterDto.ReadResponse> getTodayLetterContents(
        @PathVariable Long letterId,
        @AuthenticationPrincipal UserDetails userDetails) {
        /* 오늘의 편지 읽기 요청 */

        log.debug(DEFAULT_URL + "/auth/" + letterId);

        return ResponseEntity.ok(letterService
            .getTodayLetterContentsByLetterId(letterId, userDetails));
    }


    @PostMapping("/admin")
    public ResponseEntity<LetterDto.ReadResponse> postLetter(
        @Valid @RequestBody LetterDto.PostRequest letterReqDto) {

        log.debug("/api/v1/letter postLetter access");
        return ResponseEntity.ok().body(letterService.postLetter(letterReqDto));
    }
}
