package net.lifepost.service.domain.letter;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lifepost.service.common.exception.CommonException;
import net.lifepost.service.common.exception.ErrorEnum;
import net.lifepost.service.domain.card.FocusTypeEnum;
import net.lifepost.service.domain.letter.LetterDto.PageResponse;
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

    // TODO 삭제 -> 편지 무료화
    @GetMapping("/open/latest")
    public ResponseEntity<LetterDto.ReadResponse> getTodayLetter() {

        return ResponseEntity.ok().body(letterService.getLatestLetter());
    }

    // TODO 삭제 -> 편지 무료화
    @GetMapping("/auth/member/{pageNo}/{size}")
    public Page<LetterDto.PageResponse> getLetters(
        @PathVariable int pageNo,
        @PathVariable int size,
        @RequestParam(value = "type") List<LetterTypeEnum> type,
        @RequestParam(value = "focusType") List<FocusTypeEnum> focusTypeList,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        Page<PageResponse> responses;
        // 소지 목록 조회 이거나 focus한 편지 조회시 getMyLetterList
        if ((type.size() == 1 && type.contains(LetterTypeEnum.MY_LETTER)) || (
            focusTypeList.size() == 1 && focusTypeList.contains(FocusTypeEnum.ATTENTION))) {
            responses = letterService.getMyLetterList(pageNo, size, focusTypeList, userDetails);
        } else if (type.size() == 1 && type.contains(LetterTypeEnum.MISS)) {
            responses = letterService.getMissLetterList(pageNo, size, userDetails);
        } else if (type.size() == 2 && type.contains(LetterTypeEnum.MY_LETTER) && type
            .contains(LetterTypeEnum.MISS) && focusTypeList.size() == 2) {
            responses = letterService
                .getAllLetterList(pageNo, size, Optional.of(userDetails));
        } else {
            throw new CommonException(ErrorEnum.LETTER_REQUEST_INVALID);
        }
        return responses;
    }

    @GetMapping("/open/{pageNo}/{size}")
    public Page<LetterDto.PageResponse> getAllLetters(
        @PathVariable int pageNo,
        @PathVariable int size) {

        return letterService
            .getAllLetterList(pageNo, size, Optional.empty());
    }

    // TODO 삭제 -> 편지 무료화
    @GetMapping("/auth/stamp/{letterId}")
    public ResponseEntity<LetterDto.ReadResponse> getLetterContentsUsingStamp(
        @PathVariable Long letterId,
        @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(letterService.getLetterContentsByLetterId(letterId, userDetails));
    }

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

    @GetMapping("/open/index/{letterId}/{size}")
    public Page<LetterDto.PageResponse> getIndexLetters(
        @PathVariable Long letterId,
        @PathVariable int size
    ) {
        return letterService.getIndexLetterList(letterId, size);
    }
}
