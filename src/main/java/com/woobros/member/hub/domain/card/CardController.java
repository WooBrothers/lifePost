package com.woobros.member.hub.domain.card;

import com.woobros.member.hub.domain.card.CardDto.PageResponse;
import com.woobros.member.hub.model.card.CardTypeEnum;
import java.net.URI;
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
    private final String schema = "/api/v1/card/auth/";

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
        return cardService.getLatestMemberCards(size, userDetails);
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
        return cardService.getMemberCards(size, memberCardId, userDetails);
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
        return cardService.getLatestMemberCustomCards(size, userDetails);
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
        return cardService.getMemberCustomCards(size, memberCustomCardId, userDetails);
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
        return cardService.getLatestFocusCards(size, userDetails);
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
        return cardService.getFocusCards(size, focusCardId, userDetails);
    }

    /**
     * cardId의 내용 읽기
     *
     * @param cardId
     * @param userDetails
     * @return
     */
    @GetMapping("/auth/{type}/{cardId}")
    public ResponseEntity<CardDto.ReadResponse> getCardContents(
        @PathVariable CardTypeEnum cardTypeEnum,
        @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(cardService.getCardContents(cardId, cardTypeEnum, userDetails));
    }

    /**
     * 멤버가 생성한 카드 내용 저장
     *
     * @param memberCardPostDto
     * @param userDetails
     * @return
     */
    @PostMapping("/auth/member/custom")
    public ResponseEntity<String> postMemberCustomCard(
        @RequestBody CardDto.PostRequest memberCardPostDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        CardDto.ReadResponse cardDto = cardService
            .postMemberCustomCard(memberCardPostDto, userDetails);

        String url = schema + cardDto.getType() + "/" + cardDto.getId();
        return ResponseEntity.created(URI.create(url)).body("member custom card created.");
    }

    /**
     * 멤버가 전달한 카드 focus 처리
     *
     * @param focusCardRequest
     * @param userDetails
     * @return
     */
    @PostMapping("/auth/focus")
    public ResponseEntity<String> postFocusCard(
        @RequestBody CardDto.PostFocusRequest focusCardRequest,
        @AuthenticationPrincipal UserDetails userDetails) {

        CardDto.ReadResponse cardDto = cardService.postFocusCard(focusCardRequest, userDetails);

        String url = schema + cardDto.getType() + "/" + cardDto.getId();
        return ResponseEntity.created(URI.create(url)).body("member focus card created.");
    }

    /**
     * backoffice 확언 카드 쓰기 기능
     *
     * @param cardPostReqDto
     * @param userDetails
     * @return
     */

    @PostMapping("/admin/affirmation")
    public ResponseEntity<String> postAffirmationCard(
        @RequestBody CardDto.PostRequest cardPostReqDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        CardDto.ReadResponse cardDto = cardService.postAffirmationCard(cardPostReqDto, userDetails);

        String url = schema + cardDto.getType() + "/" + cardDto.getId();
        return ResponseEntity.created(URI.create(url)).body("affirmation card created.");
    }
}
