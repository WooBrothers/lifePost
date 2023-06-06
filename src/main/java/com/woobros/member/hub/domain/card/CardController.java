package com.woobros.member.hub.domain.card;

import com.woobros.member.hub.model.card.CardDto;
import com.woobros.member.hub.model.card.CardDto.ReadResponse;
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
@RequestMapping("/api/v1/card")
public class CardController {

    private final CardService cardService;

    @PostMapping("/admin/affirmation")
    public ResponseEntity<ReadResponse> postAffirmationCard(
        @RequestBody CardDto.PostRequest cardPostReqDto) {

        log.debug("/api/v1/card postAffirmationCard access");
        return ResponseEntity.ok(cardService.postCard(cardPostReqDto));
    }

    @PostMapping("/auth/member")
    public ResponseEntity<CardDto.ReadResponse> postMemberCard(
        @RequestBody CardDto.PostRequest memberCardPostDto) {
        log.debug("/api/v1 postAffirmationCard access");
//        return ResponseEntity.ok(cardService.postCard(cardPostReqDto));
        return null;
    }

    @PostMapping("/auth/focus")
    public ResponseEntity<CardDto.ReadResponse> postFocusCard(
        @RequestBody CardDto.PostFocusRequest focusCardRequest) {
        return null;
    }

    @GetMapping("/open/today")
    public ResponseEntity<CardDto.PageResponse> getTodayCard() {
        log.debug("/api/v1/card getTodayCard access");
        return ResponseEntity.ok(cardService.getLatestCard());
    }

    @GetMapping("/auth/focus")
    public ResponseEntity<CardDto.ReadResponse> getFocusCards() {
        log.debug("/api/v1/card getMemberLetterAffirmationCards access");
//        return ResponseEntity.ok(cardService.getLatestCard());
        return null;
    }

    @GetMapping("/auth/limited-affirmation")
    public ResponseEntity<CardDto.ReadResponse> getLimitedCards() {
        log.debug("/api/v1/card getMemberLetterAffirmationCards access");
//        return ResponseEntity.ok(cardService.getLatestCard());
        return null;
    }
}
