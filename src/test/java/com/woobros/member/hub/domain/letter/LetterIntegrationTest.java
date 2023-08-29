package com.woobros.member.hub.domain.letter;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woobros.member.hub.domain.card.FocusTypeEnum;
import com.woobros.member.hub.model.card.affr_card.AffirmationCard;
import com.woobros.member.hub.model.card.affr_card.AffirmationCardRepository;
import com.woobros.member.hub.model.card.memb_card.MemberCard;
import com.woobros.member.hub.model.card.memb_card.MemberCardRepository;
import com.woobros.member.hub.model.letter.Letter;
import com.woobros.member.hub.model.letter.LetterMapper;
import com.woobros.member.hub.model.letter.LetterRepository;
import com.woobros.member.hub.model.member.Member;
import com.woobros.member.hub.model.member.MemberRepository;
import com.woobros.member.hub.model.member.Role;
import com.woobros.member.hub.model.member_letter.MemberLetter;
import com.woobros.member.hub.model.member_letter.MemberLetterRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class LetterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LetterRepository letterRepository;
    @Autowired
    private LetterMapper letterMapper;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberLetterRepository memberLetterRepository;
    @Autowired
    private MemberCardRepository memberCardRepository;
    @Autowired
    private AffirmationCardRepository affirmationCardRepository;

    private final String authorization = "Authorization";
    private final String tokenType = "Bearer ";
    private final String loginRedirectUrl = "http://localhost/login/page";

    @Value("${test.accessToken}")
    private String testAccessToken;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void beforeEach() {

        // given
        Member member = memberRepository.findById(1L).orElseThrow(
            () -> new RuntimeException("member not found")
        );
        PageRequest pageRequest = PageRequest.of(0, 5);

        // 14 13 12 11 10 편지 저장 처리
        Page<Letter> letters = letterRepository.findByIdLessThanOrderByIdDesc(15L, pageRequest);

        letters.forEach(letter -> memberLetterRepository.save(
            MemberLetter.builder()
                .member(member)
                .letter(letter)
                .focus(FocusTypeEnum.ATTENTION)
                .build()));
    }

    @Test
    void testPostLetter_WhenHaveUserToken_WillForbidden() throws Exception {
        Letter letter = Letter.builder()
            .title("test16")
            .contents("think every time.")
            .writer("wookim")
            .build();

        LetterDto.PostRequest postRequest = letterMapper.toRequestDto(letter);

        ResultActions response = mockMvc.perform(post("/api/v1/letter/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken)
            .content(objectMapper.writeValueAsString(postRequest))
        );

        response.andDo(print())
            .andExpect(status().isForbidden());

    }


    @Test
    void testGetTodayLetter_WhenNoHaveToken_WillOk() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/letter/open/latest")
            .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(14)))
            .andExpect(jsonPath("$.title", is("test14")));
    }

    @Test
    void testGetTodayLetter_WhenHaveUserToken_WillOk() throws Exception {
        // USER role
        ResultActions response = mockMvc.perform(get("/api/v1/letter/open/latest")
            .header(authorization, tokenType + testAccessToken)
            .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(14)))
            .andExpect(jsonPath("$.title", is("test14")));
    }

    @Test
    void testGetTodayLetter_WhenHaveAdminToken_WillOk() throws Exception {
        // ADMIN role
        Member member = memberRepository.findById(1L).orElseThrow();
        member.setRole(Role.ADMIN);

        ResultActions response = mockMvc.perform(get("/api/v1/letter/open/latest")
            .header(authorization, tokenType + testAccessToken)
            .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(14)))
            .andExpect(jsonPath("$.title", is("test14")));
    }


    @Test
    void testGetTodayLetterContents_WhenNoHaveToken_Will302() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/letter/auth/15")
            .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    void testGetTodayLetterContents_WhenHaveGuestToken_WillForbidden() throws Exception {
        Member member = memberRepository.findById(1L).orElseThrow();
        member.setRole(Role.GUEST);

        ResultActions response = mockMvc.perform(get("/api/v1/letter/auth/15")
            .header(authorization, tokenType + testAccessToken)
            .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print()).andExpect(status().isForbidden());
    }


    @Test
    void testGetTodayLetterContents_WhenHaveAdminToken_WillForbidden() throws Exception {
        Member member = memberRepository.findById(1L).orElseThrow();
        member.setRole(Role.ADMIN);

        ResultActions response = mockMvc.perform(get("/api/v1/letter/auth/15")
            .header(authorization, tokenType + testAccessToken)
            .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    void testGetLetterContentsUsingStamp_WhenNoHaveToken_Will302() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/letter/auth/stamp/14")
            .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    void testGetLetterContentsUsingStamp_WhenHaveGuestToken_WillForbidden() throws Exception {
        Member member = memberRepository.findById(1L).orElseThrow();
        member.setRole(Role.GUEST);

        ResultActions response = mockMvc.perform(get("/api/v1/letter/auth/stamp/14")
            .header(authorization, tokenType + testAccessToken)
            .contentType(MediaType.APPLICATION_JSON));

        String forwardUrl = "/forbidden/page";
        response.andDo(print()).andExpect(status().isForbidden())
            .andExpect(forwardedUrl(forwardUrl))
        ;
    }

    @Test
    void testGetLetterContentsUsingStamp_WhenHaveUserToken_WillOk() throws Exception {
        // 정삭적인 요청일 때 member_letter와 member_card 가 잘 저장되는지 확인
        ResultActions response = mockMvc.perform(get("/api/v1/letter/auth/stamp/9")
            .header(authorization, tokenType + testAccessToken)
            .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(9)))
            .andExpect(jsonPath("$.title", is("test9")))
            .andExpect(jsonPath("$.contents", is("test9")));

        Optional<MemberLetter> memberLetter = memberLetterRepository
            .findByMemberIdAndLetterId(1L, 9L);
        assert memberLetter.isPresent();

        List<Long> affirmationCardIdList = affirmationCardRepository.findByLetterId(9L).stream()
            .map(AffirmationCard::getId).collect(
                Collectors.toList());

        List<Long> memberCardIdList = memberCardRepository
            .findByMemberLetterId(memberLetter.get().getId())
            .stream().map(MemberCard::getAffirmationCard)
            .map(AffirmationCard::getId)
            .collect(Collectors.toList());

        assert affirmationCardIdList.size() == memberCardIdList.size();
        assertThat(affirmationCardIdList, containsInAnyOrder(memberCardIdList.toArray()));
    }

    @Test
    void testGetLetterContentsUsingStamp_WhenHaveUserToken_InvalidLetterId_Will409()
        throws Exception {
        // stamp 사용 시 오늘의 편지 id를 요청하면 안됨 이를 테스트
        ResultActions response = mockMvc.perform(get("/api/v1/letter/auth/stamp/15")
            .header(authorization, tokenType + testAccessToken)
            .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print()).andExpect(status().isConflict())
            .andExpect(jsonPath("$.errorCode", is("LT0001")))
        ;
    }


}
