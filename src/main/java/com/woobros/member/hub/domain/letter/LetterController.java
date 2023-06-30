package com.woobros.member.hub.domain.letter;

import java.net.URI;
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

    @GetMapping("/open/latest")
    public ResponseEntity<LetterDto.ReadResponse> getTodayLetter() {

        return ResponseEntity.ok().body(letterService.getLatestLetter());
    }

    @GetMapping("/open/page/{size}/{lastLetterId}")
    public Page<LetterDto.PageResponse> getLettersPage(
        @PathVariable int size,
        @PathVariable Long lastLetterId) {

        return letterService.getLettersPage(lastLetterId, size);
    }

    @GetMapping("/auth/{letterId}")
    public ResponseEntity<LetterDto.ReadResponse> getTodayLetterContents(
        @PathVariable Long letterId,
        @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(letterService
            .getTodayLetterContentsByLetterId(letterId, userDetails));
    }

    @GetMapping("/auth/stamp/{letterId}")
    public ResponseEntity<LetterDto.ReadResponse> getLetterContentsUsingStamp(
        @PathVariable Long letterId,
        @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(letterService.getLetterContentsByLetterId(letterId, userDetails));
    }

    @GetMapping("/auth/page/{size}")
    public Page<LetterDto.PageResponse> getHaveLatestLetterPage(
        @PathVariable int size,
        @AuthenticationPrincipal UserDetails userDetails) {

        return letterService.getHaveLatestLetterPage(size, userDetails);
    }

    @GetMapping("/auth/page/{size}/{letterId}")
    public Page<LetterDto.PageResponse> getHaveLetterPage(
        @PathVariable Long letterId, @PathVariable int size,
        @AuthenticationPrincipal UserDetails userDetails) {

        return letterService.getHaveLetterPage(letterId, size, userDetails);
    }

    @GetMapping("/auth/page/no-have/{size}")
    public Page<LetterDto.PageResponse> getDoesNotHaveLatestLetterPage(
        @PathVariable int size,
        @AuthenticationPrincipal UserDetails userDetails) {

        return letterService.getDoesNotHaveLatestLetterPage(size, userDetails);
    }

    @GetMapping("/auth/page/no-have/{size}/{letterId}")
    public Page<LetterDto.PageResponse> getDoesNotHaveLetterPage(
        @PathVariable int size,
        @PathVariable Long letterId,
        @AuthenticationPrincipal UserDetails userDetails) {

        return letterService.getDoesNotHaveLetterPage(letterId, size, userDetails);
    }

    /** backoffice */

    /**
     * @param letterReqDto
     * @return
     */
    @PostMapping("/admin")
    public ResponseEntity<String> postLetter(
        @Valid @RequestBody LetterDto.PostRequest letterReqDto) {

        LetterDto.ReadResponse readResponse = letterService.postLetter(letterReqDto);

        String url = "/api/v1/letter/auth/" + readResponse.getId();
        return ResponseEntity.created(URI.create(url)).body("letter is created.");
    }
}
