package com.woobros.member.hub.domain.card;

import com.woobros.member.hub.domain.card.CardDto.PageResponse;
import com.woobros.member.hub.domain.card.CardDto.ReadResponse;
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
@RequestMapping("/api/v1/card")
public class CardController {

    private final CardService cardService;

    /**
     * 멤버가 소유한 최신 카드를 size 만큼 조회 (리스트 출력용)
     *
     * @param size
     * @param userDetails
     * @return
     */

    @GetMapping("/auth/member/{size}")
    public Page<CardDto.PageResponse> getLatestMemberCards(
        @PathVariable int size,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return null;
    }

    /**
     * 멤버가 소유한 카드중 전달한 memberCardId 이후의 최신 카드를 size 만큼 조회 (리스트 출력용)
     *
     * @param size
     * @param memberCardId
     * @param userDetails
     * @return
     */
    @GetMapping("/auth/member/{size}/{memberCardId}")
    public Page<CardDto.PageResponse> getMemberCards(
        @PathVariable int size,
        @PathVariable Long memberCardId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return null;
    }

    /**
     * 멤버가 만든 최신 카드를 size 만큼 조회 (리스트 출력용)
     *
     * @param size
     * @param userDetails
     * @return
     */
    @GetMapping("/auth/custom/{size}")
    public Page<PageResponse> getLatestMemberCustomCards(
        @PathVariable int size,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return null;
    }

    /**
     * 멤버가 만든 카드중 전달한 memberCustomCardId 이후의 최신 카드를 size 만큼 조회 (리스트 출력용)
     *
     * @param size
     * @param memberCustomCardId
     * @param userDetails
     * @return
     */
    @GetMapping("/auth/custom/{size}/{memberCustomCardId}")
    public Page<PageResponse> getMemberCustomCards(
        @PathVariable int size,
        @PathVariable Long memberCustomCardId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return null;
    }

    /**
     * 유저의 최신 focus 카드 size 만큼 조회 (리스트 출력용)
     *
     * @param size
     * @param userDetails
     * @return
     */
    @GetMapping("/auth/focus/{size}")
    public Page<PageResponse> getLatestFocusCards(
        @PathVariable int size,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return null;
    }

    /**
     * 멤버가 focus 한 카드중 전달한 focusCardId 이후의 최신 카드를 size 만큼 조회 (리스트 출력용)
     *
     * @param size
     * @param focusCardId
     * @param userDetails
     * @return
     */
    @GetMapping("/auth/focus/{size}/{focusCardId}")
    public Page<PageResponse> getFocusCards(
        @PathVariable int size,
        @PathVariable Long focusCardId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return null;
    }

    /**
     * cardId의 내용 읽기
     *
     * @param cardId
     * @param userDetails
     * @return
     */
    @GetMapping("/auth/{cardId}")
    public ResponseEntity<CardDto.ReadResponse> getCardContents(
        @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return null;
    }

    /**
     * 멤버가 생성한 카드 내용 저장
     *
     * @param memberCardPostDto
     * @param userDetails
     * @return
     */
    @PostMapping("/auth/member/custom")
    public ResponseEntity<CardDto.ReadResponse> postMemberCustomCard(
        @RequestBody CardDto.PostRequest memberCardPostDto,
        @AuthenticationPrincipal UserDetails userDetails) {
        return null;
    }

    /**
     * 멤버가 전달한 카드 focus 처리
     *
     * @param focusCardRequest
     * @param userDetails
     * @return
     */
    @PostMapping("/auth/focus")
    public ResponseEntity<CardDto.ReadResponse> postFocusCard(
        @RequestBody CardDto.PostFocusRequest focusCardRequest,
        @AuthenticationPrincipal UserDetails userDetails) {
        return null;
    }

    /**
     * backoffice 확언 카드 쓰기 기능
     *
     * @param cardPostReqDto
     * @param userDetails
     * @return
     */

    @PostMapping("/admin/affirmation")
    public ResponseEntity<ReadResponse> postAffirmationCard(
        @RequestBody CardDto.PostRequest cardPostReqDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(cardService.postCard(cardPostReqDto));
    }
}
