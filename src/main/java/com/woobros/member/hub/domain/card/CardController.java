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
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private static final String SCHEMA = "/api/v1/card/auth/";

//    /**
//     * 멤버가 소유한 최신 카드를 size 만큼 조회 (리스트 출력용)
//     *
//     * @param size        불러올 카드 사이즈
//     * @param userDetails security 멤버 정보
//     * @return Page 처리된 카드 정보 (컨텐츠 x)
//     */
//
//    @GetMapping("/auth/member/{size}")
//    public Page<CardDto.PageResponse> getLatestMemberCards(
//        @PathVariable int size,
//        @AuthenticationPrincipal UserDetails userDetails
//    ) {
//        return cardService.getLatestMemberCards(size, userDetails);
//    }

    /**
     * 멤버가 소유한 카드중 전달한 memberCardId 이후의 최신 카드를 size 만큼 조회 (리스트 출력용)
     *
     * @param size        불러올 카드 사이즈
     * @param pageNo      조회할 페이지 번호
     * @param userDetails security 멤버 정보
     * @return Page 처리된 카드 정보 (컨텐츠 x)
     */
    @GetMapping("/auth/member/{pageNo}/{size}")
    public Page<CardDto.PageResponse> getMemberCards(
        @PathVariable int pageNo,
        @PathVariable int size,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return cardService.getMemberCards(size, pageNo, userDetails);
    }

//    /**
//     * 멤버가 만든 최신 카드를 size 만큼 조회 (리스트 출력용)
//     *
//     * @param size        불러올 카드 사이즈
//     * @param userDetails security 유저 정보
//     * @return Page 처리된 카드 정보 (컨텐츠 x)
//     */
//    @GetMapping("/auth/custom/{size}")
//    public Page<PageResponse> getLatestMemberCustomCards(
//        @PathVariable int size,
//        @AuthenticationPrincipal UserDetails userDetails
//    ) {
//        return cardService.getLatestMemberCustomCards(size, userDetails);
//    }

    /**
     * 멤버가 만든 카드중 전달한 memberCustomCardId 이후의 최신 카드를 size 만큼 조회 (리스트 출력용)
     *
     * @param size        불러올 카드 사이즈
     * @param pageNo      조회할 페이지 번호
     * @param userDetails security 유저 정보
     * @return Page 처리된 카드 정보 (컨텐츠 x)
     */
    @GetMapping("/auth/custom/{pageNo}/{size}")
    public Page<PageResponse> getMemberCustomCards(
        @PathVariable int pageNo,
        @PathVariable int size,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return cardService.getMemberCustomCards(size, pageNo, userDetails);
    }

//    /**
//     * 유저의 최신 focus 카드 size 만큼 조회 (리스트 출력용)
//     *
//     * @param size        불러올 카드 사이즈
//     * @param userDetails security 유저 정보
//     * @return Page 처리된 카드 정보 (컨텐츠 x)
//     */
//    @GetMapping("/auth/focus/{size}")
//    public Page<PageResponse> getLatestFocusCards(
//        @PathVariable int size,
//        @AuthenticationPrincipal UserDetails userDetails
//    ) {
//        return cardService.getLatestFocusCards(size, userDetails);
//    }

    /**
     * 멤버가 focus 한 카드중 전달한 memberCardId 이후의 최신 카드를 size 만큼 조회 (리스트 출력용)
     * <p>
     * memberCard의 리스트 중 focus 상태인 것들만 조회하는 리스트 뷰에서 사용한다. 전달하는 memberCardId 이후에 focus 상태인 리스트 정보를
     * 조회한다.
     *
     * @param size        불러올 카드 사이즈
     * @param pageNo      멤버가 소유한 카드의 아이디
     * @param userDetails security 멤버 정보
     * @return Page 처리된 카드 정보 (컨텐츠 x)
     */
    @GetMapping("/auth/focus/{pageNo}/{size}")
    public Page<PageResponse> getFocusCards(
        @PathVariable int pageNo,
        @PathVariable int size,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return cardService.getFocusCards(size, pageNo, userDetails);
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
        @RequestBody CardDto.PostCustomRequest memberCustomCardPostDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        CardDto.ReadResponse cardDto = cardService
            .postMemberCustomCard(memberCustomCardPostDto, userDetails);

        String url = SCHEMA + cardDto.getType().toString() + "/" + cardDto.getId();
        return ResponseEntity.created(URI.create(url)).body("member custom card created.");
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
        @RequestBody CardDto.PostFocusRequest focusCardRequest,
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
        @RequestBody CardDto.PostFocusRequest focusCardRequest,
        @AuthenticationPrincipal UserDetails userDetails) {

        cardService.deleteFocusCard(focusCardRequest, userDetails);

        return ResponseEntity.noContent().build();
    }

    /**
     * backoffice 확언 카드 쓰기 기능
     *
     * @param cardPostReqDto 확언 카드의 내용을 담은 dto
     * @param userDetails    security 멤버 정보
     * @return 성공 msg 와 header > location: 생성한 리소스를 확인 가능한 url
     */
    @PostMapping("/admin/affirmation")
    public ResponseEntity<String> postAffirmationCard(
        @RequestBody CardDto.PostRequest cardPostReqDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        CardDto.ReadResponse cardDto = cardService.postAffirmationCard(cardPostReqDto, userDetails);

        String url = SCHEMA + cardDto.getType() + "/" + cardDto.getId();
        return ResponseEntity.created(URI.create(url)).body("affirmation card created.");
    }


}
