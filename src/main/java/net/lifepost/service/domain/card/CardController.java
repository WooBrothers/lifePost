package net.lifepost.service.domain.card;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lifepost.service.model.card.CardTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/v1/card")
public class CardController {

    private final CardService cardService;
    private static final String SCHEMA = "/api/v1/card/auth/";

    /**
     * 멤버가 소유한 카드중 전달한 페이 번호 이후의 카드를 size 만큼 조회 (리스트 출력용)
     *
     * @param size         불러올 카드 사이즈
     * @param memberCardId 조회할 카드 번호
     * @param userDetails  security 멤버 정보
     * @param focus        focus 한 정보 조회
     * @param type         카드 종류
     * @return Page 처리된 카드 정보지
     */
    @GetMapping("/auth/member/{memberCardId}/{size}")
    public Page<CardDto.PageResponse> getMemberCards(
        @PathVariable Long memberCardId,
        @PathVariable int size,
        @RequestParam(value = "focus", required = false) Optional<FocusTypeEnum> focus,
        @RequestParam(value = "type", required = false) List<CardTypeEnum> type,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return cardService.getMemberCards(size, memberCardId, focus, type, userDetails);
    }

    @GetMapping("/auth/focus/after/{memberCardId}/{size}")
    public ResponseEntity<Page<CardDto.PageResponse>> getCardListAfterCardId(
        @PathVariable Long memberCardId,
        @PathVariable int size,
        @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity
            .ok(cardService.getCardListAfterCardId(memberCardId, size, userDetails));
    }

    /**
     * cardId의 내용 읽기
     *
     * @param cardId      읽을 카드의 아이디
     * @param userDetails security 멤버 정보
     * @return 카드 내용 전달 (컨텐츠 o)
     */
    @GetMapping("/auth/{cardTypeEnum}/{cardId}")
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
     * @param memberCustomCardPostDto 멤버가 생성할 카드의 정보를 담은 dto
     * @param userDetails             security 멤버 정보
     * @return 성공 msg 와 header > location: 생성한 리소스를 확인 가능한 url
     */
    @PostMapping("/auth/member/custom")
    public ResponseEntity<String> postMemberCustomCard(
        @RequestBody @Valid CardDto.PostCustomRequest memberCustomCardPostDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        CardDto.ReadResponse cardDto = cardService
            .postMemberCustomCard(memberCustomCardPostDto, userDetails);

        String url = SCHEMA + cardDto.getType().toString() + "/" + cardDto.getId();
        return ResponseEntity.created(URI.create(url)).body("member custom card created.");
    }

    @PostMapping("/auth/member/custom/delete")
    public ResponseEntity<String> deleteMemberCustomCard(
        @RequestBody @Valid CardDto.DeleteCustomRequest deleteCustomRequest,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        cardService.deleteCustomCard(deleteCustomRequest, userDetails);

        return ResponseEntity.ok("member custom card deleted");
    }

    /**
     * 멤버가 전달한 카드 focus 처리
     *
     * @param focusCardRequest focus할 카드의 정보를 담은 dto
     * @param userDetails      security 멤버 정보
     * @return 성공 msg 와 header > location: 생성한 리소스를 확인 가능한 url
     */
    @PostMapping("/auth/focus")
    public ResponseEntity<String> postFocusCard(
        @RequestBody @Valid CardDto.PostFocusRequest focusCardRequest,
        @AuthenticationPrincipal UserDetails userDetails) {

        CardDto.ReadResponse cardDto = cardService.postFocusCard(focusCardRequest, userDetails);

        String url =
            SCHEMA + focusCardRequest.getType().toString() + "/" + cardDto.getId();
        return ResponseEntity.created(URI.create(url)).body("member focus card created.");
    }

    /**
     * @param userDetails security 멤버 정보
     * @return focus 해제 성공 msg
     */
    @DeleteMapping("/auth/focus")
    public ResponseEntity<String> deleteFocusCard(
        @RequestBody @Valid CardDto.PostFocusRequest focusCardRequest,
        @AuthenticationPrincipal UserDetails userDetails) {

        cardService.deleteFocusCard(focusCardRequest, userDetails);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auth/write_count")
    public ResponseEntity<String> postCardWrite(
        @RequestBody @Valid CardDto.PostWriteRequest postWriteRequest,
        @AuthenticationPrincipal UserDetails userDetails) {

        cardService.postCardWrite(postWriteRequest, userDetails);
        return ResponseEntity.created(URI.create("/")).body("write count saved.");
    }

    @PostMapping("/auth/write_count/reward")
    public ResponseEntity<String> postCardWriteReward(
        @AuthenticationPrincipal UserDetails userDetails) {

        cardService.postCardWriteReward(userDetails);
        return ResponseEntity.created(URI.create("/")).body("reward saved.");
    }
}
