package net.lifepost.service.domain.letter;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lifepost.service.domain.card.FocusTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/letter")
public class LetterController {

    /* beans */
    private final LetterService letterService;

    @GetMapping("/open/{letterId}")
    public ResponseEntity<LetterDto.ReadResponse> getOpenLetterContents(
        @PathVariable Long letterId) {

        return ResponseEntity.ok(letterService.getOpenLetterContents(letterId));
    }

    @PostMapping("/auth/focus")
    public ResponseEntity<String> postFocusMemberLetter(
        @Valid @RequestBody LetterDto.PostFocusRequest letterReqDto,
        @AuthenticationPrincipal UserDetails userDetails) {
        letterService.postFocusLetter(letterReqDto, userDetails);

        return ResponseEntity
            .created(URI.create("/api/v1/letter/auth/" + letterReqDto.getLetterId()))
            .body("member letter is focus request success.");
    }

    @GetMapping("/auth/member/{letterId}/{size}")
    public Page<LetterDto.PageResponse> getMemberLetterList(
        @PathVariable Long letterId,
        @PathVariable int size,
        @RequestParam(value = "focusType") List<FocusTypeEnum> focusTypeList,
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        return letterService.getMemberLetterList(letterId, size, focusTypeList, userDetails);
    }

    @GetMapping("/open/index/{postDate}/{size}")
    public Page<LetterDto.PageResponse> getIndexLetters(
        @PathVariable LocalDate postDate,
        @PathVariable int size
    ) {
        return letterService.getIndexLetterList(postDate, size);
    }
}
