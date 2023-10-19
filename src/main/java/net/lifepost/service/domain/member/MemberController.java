package net.lifepost.service.domain.member;

import lombok.RequiredArgsConstructor;
import net.lifepost.service.model.member.MemberDto.InfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/auth/info")
    public ResponseEntity<InfoResponse> getMemberInfo(
        @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(memberService.getMemberInfo(userDetails));
    }

    @PostMapping("/auth/withdraw")
    public ResponseEntity<String> withdraw(@AuthenticationPrincipal UserDetails userDetails) {
        memberService.withdraw(userDetails);
        return ResponseEntity.ok().body("withdraw success. goodbye.");
    }
}
