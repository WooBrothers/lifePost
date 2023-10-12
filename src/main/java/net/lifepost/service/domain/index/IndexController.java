package net.lifepost.service.domain.index;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lifepost.service.common.exception.CommonException;
import net.lifepost.service.common.exception.ErrorEnum;
import net.lifepost.service.model.letter.Letter;
import net.lifepost.service.model.letter.LetterRepository;
import net.lifepost.service.model.member.Member;
import net.lifepost.service.model.member.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {

    /* beans */
    private final MemberRepository memberRepository;
    private final LetterRepository letterRepository;

    @Value("${url}")
    private String url;

    @GetMapping("/")
    public String index(Model model) throws JsonProcessingException {
        model.addAttribute("ogImage", url + "/img/full-logo.png");
        model.addAttribute("ogUrl", url);
        return "index";
    }

    @GetMapping("/login/page")
    public String login() {
        return "login";
    }

    @GetMapping("/forbidden/page")
    public String forbidden() {
        return "forbidden";
    }

    @GetMapping("/card/list/page")
    public String getCardListPage() {
        return "contents/card/cardList";
    }

    @GetMapping("/card/custom/page")
    public String getCardCustomPage() {
        return "contents/card/cardCreate";
    }

    @GetMapping("/card/write/page")
    public String getCardWritePage() {
        return "contents/card/cardWrite";
    }

    @GetMapping("/letter/list/page")
    public String getLetterListPage() {
        return "contents/letter/letterList";
    }

    @GetMapping("/letter/list/open/page")
    public String getLetterOpenListPage() {
        return "contents/letter/letterOpenList";
    }

    @GetMapping("/letter/read/page/{letterId}")
    public String getLetterReadPage(@PathVariable Long letterId, Model model) {
        Letter letter = letterRepository.findById(letterId)
            .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));

        model.addAttribute("letterId", letterId);
        model.addAttribute("ogTitle", letter.getTitle());
        model.addAttribute("ogDescription", letter.getContents());
        model.addAttribute("ogImage", letter.getLetterImage());
        model.addAttribute("ogUrl", url + "/letter/read/page/" + letterId);

        return "contents/letter/letterRead";
    }

    @GetMapping("/mypage/page")
    public String getMyPage() {
        return "mypage";
    }

    @GetMapping("/withdraw/page")
    public String getWithdrawPage() {
        return "widthdraw";
    }

    @GetMapping("/landing/page")
    public String getLandingPage() {
        return "introduce/landing";
    }

    @GetMapping("/api/v1/letter/auth/stamp/popup")
    public String getStampUsePopUpPage(Model model,
        @AuthenticationPrincipal UserDetails userDetails) {

        Member member = memberRepository
            .findByEmail(userDetails.getUsername())
            .orElseThrow(() ->
                new CommonException(ErrorEnum.NOT_FOUND)
            );

        model.addAttribute("email", member.getEmail());
        model.addAttribute("stamp", member.getStampCount());

        return "contents/letter/letterStampUseModal";
    }

    @GetMapping("/auth/new/token")
    public ResponseEntity<Map<String, String>> getVerifiedToken() {
        /**
         * cookie에 토큰이 있는 유저일 경우
         * access Token가 만료됬는지 확인을 위한 메소드
         *
         * 실제 확인 로직은 security를 이용한다.QA
         *
         * [플로우]
         * 1. 멤버 -> lifePost url 접근
         * 2. 브라우저 쿠키 확인 -> accessToken 여부 확인
         * 3. 있을 경우 -> 해당 api call
         * 4. spring security 토큰 확인
         * 5. 만료 되었을 경우 401
         * 6. 아닐 경우 문제 없음
         * 7. 만료 되었을 경우 refresh token을 이용해 accessToken 최신화
         * 8. 이후 api call 시 401 문제 안나도록 세팅
         * */
        log.info("getVerifiedToken called.");

        Map<String, String> result = new HashMap<>();
        result.put("result", "ok");

        return ResponseEntity.ok(result);
    }

}
