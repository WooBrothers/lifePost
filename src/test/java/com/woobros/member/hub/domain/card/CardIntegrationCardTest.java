package com.woobros.member.hub.domain.card;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woobros.member.hub.domain.card.CardDto.PostFocusRequest;
import com.woobros.member.hub.domain.card.CardDto.PostRequest;
import com.woobros.member.hub.model.card.CardTypeEnum;
import com.woobros.member.hub.model.card.affr_card.AffirmationCard;
import com.woobros.member.hub.model.card.affr_card.AffirmationCardRepository;
import com.woobros.member.hub.model.card.memb_card.MemberCard;
import com.woobros.member.hub.model.card.memb_card.MemberCardRepository;
import com.woobros.member.hub.model.card.memb_cust_card.MemberCustomCard;
import com.woobros.member.hub.model.card.memb_cust_card.MemberCustomCardRepository;
import com.woobros.member.hub.model.letter.Letter;
import com.woobros.member.hub.model.letter.LetterMapper;
import com.woobros.member.hub.model.letter.LetterRepository;
import com.woobros.member.hub.model.member.Member;
import com.woobros.member.hub.model.member.MemberRepository;
import com.woobros.member.hub.model.member_letter.MemberLetter;
import com.woobros.member.hub.model.member_letter.MemberLetterRepository;
import java.util.List;
import javax.transaction.Transactional;
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
public class CardIntegrationCardTest {

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
    private MemberCustomCardRepository memberCustomCardRepository;
    @Autowired
    private AffirmationCardRepository affirmationCardRepository;

    private final String authorization = "Authorization";
    private final String tokenType = "Bearer ";
    private final String loginRedirectUrl = "http://localhost/login/page";

    @Value("${test.accessToken}")
    private String testAccessToken;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Member member;

    @BeforeEach
    void beforeEach() {

        // given
        member = memberRepository.findById(1L).orElseThrow(
            () -> new RuntimeException("member not found")
        );
        PageRequest pageRequest = PageRequest.of(0, 5);

        // 14 13 12 11 10 편지 저장 처리
        Page<Letter> letters = letterRepository.findByIdLessThanOrderByIdDesc(15L, pageRequest);

        letters.forEach(letter -> {
                MemberLetter memberLetter = memberLetterRepository.save(
                    MemberLetter.builder()
                        .member(member)
                        .letter(letter)
                        .build());

                // 14 13 12 11 10 편지에 딸린 카드 memberCard 저장처리
                List<AffirmationCard> cards = affirmationCardRepository.findByLetterId(letter.getId());

                // 14 13 12 11 까지만 memberCard 에 저장
                cards.stream()
                    .map(card ->
                        MemberCard.builder()
                            .member(member)
                            .affirmationCard(card)
                            .memberLetter(memberLetter)
                            .focus(FocusTypeEnum.NON)
                            .type(CardTypeEnum.AFFIRMATION)
                            .build()
                    ).forEach(memberCardRepository::save);
            }
        );

        MemberCustomCard memberCustomCard = MemberCustomCard.builder()
            .member(member)
            .contents("member custom card create1")
            .title("test 1 is focus")
            .build();
        MemberCustomCard memberCustomCard2 = MemberCustomCard.builder()
            .member(member)
            .contents("member custom card create2")
            .title("test 2")
            .build();
        MemberCustomCard memberCustomCard3 = MemberCustomCard.builder()
            .member(member)
            .contents("member custom card create3")
            .title("test 3")
            .build();

        memberCustomCardRepository.save(memberCustomCard);
        memberCustomCardRepository.save(memberCustomCard2);
        memberCustomCardRepository.save(memberCustomCard3);

        memberCardRepository.save(MemberCard.builder()
            .memberCustomCard(memberCustomCard)
            .member(member)
            .type(CardTypeEnum.CUSTOM)
            .focus(FocusTypeEnum.ATTENTION)
            .build());
        memberCardRepository.save(MemberCard.builder()
            .memberCustomCard(memberCustomCard2)
            .type(CardTypeEnum.CUSTOM)
            .member(member)
            .focus(FocusTypeEnum.NON)
            .build());
        memberCardRepository.save(MemberCard.builder()
            .memberCustomCard(memberCustomCard3)
            .type(CardTypeEnum.CUSTOM)
            .member(member)
            .focus(FocusTypeEnum.NON)
            .build());
    }

    @Test
    void testPostLetter_WhenHaveUserToken_WillForbidden() throws Exception {

    }

    @Test
    void testGetLatestMemberCards_WhenHaveUserToken_WillOk() throws Exception {

        // given

        MemberCustomCard memberCustomCard = memberCustomCardRepository
            .save(MemberCustomCard.builder()
                .title("test custom")
                .contents("test custom")
                .member(member)
                .build());

        memberCardRepository.save(
            MemberCard.builder()
                .member(member)
                .memberCustomCard(memberCustomCard)
                .focus(FocusTypeEnum.NON)
                .type(CardTypeEnum.CUSTOM)
                .build()
        );

        ResultActions response = mockMvc.perform(get("/api/v1/card/auth/member/3")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testGetMemberCards_WhenHaveUserToken_WillOk() throws Exception {
        ResultActions response = mockMvc
            .perform(get("/api/v1/card/auth/member/3/10")
                .contentType(MediaType.APPLICATION_JSON)
                .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    void testGetLatestMemberCustomCards() throws Exception {

        ResultActions response = mockMvc.perform(get("/api/v1/card/auth/custom/3")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testGetMemberCustomCards() throws Exception {
        ResultActions response = mockMvc
            .perform(get("/api/v1/card/auth/custom/3/13")
                .contentType(MediaType.APPLICATION_JSON)
                .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testGetLatestFocusCards() throws Exception {
        Page<MemberCard> memberCardPage = memberCardRepository
            .findByMemberIdAndIdLessThanOrderByCreatedAtDesc(
                member.getId(), 15L, PageRequest.of(0, 2));

        memberCardPage
            .forEach(card -> {
                card.setFocus(FocusTypeEnum.ATTENTION);
                memberCardRepository.save(card);
            });

        Page<MemberCustomCard> memberCustomCards = memberCustomCardRepository
            .findByMemberIdOrderByCreatedAtDesc(member.getId(), PageRequest.of(0, 1));

        ResultActions response = mockMvc.perform(get("/api/v1/card/auth/focus/3")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    void testGetFocusCards() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/card/auth/focus/{size}/{focusCardId}")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    void testGetCardContents() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/card/auth/{cardId}")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    void testPostMemberCustomCard() throws Exception {

        CardDto.PostRequest PostRequest = new PostRequest();
        ResultActions response = mockMvc.perform(post("/api/v1/card/auth/member/custom")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken)
            .content(objectMapper.writeValueAsString(PostRequest)));

        response.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testPostFocusCard() throws Exception {

        CardDto.PostFocusRequest PostRequest = new PostFocusRequest();
        ResultActions response = mockMvc.perform(post("/api/v1/card/auth/focus")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken)
            .content(objectMapper.writeValueAsString(PostRequest)));

        response.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testPostAffirmationCard() throws Exception {

        CardDto.PostRequest PostRequest = new PostRequest();
        ResultActions response = mockMvc.perform(post("/api/v1/card/admin/affirmation")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken)
            .content(objectMapper.writeValueAsString(PostRequest)));

        response.andDo(print())
            .andExpect(status().isOk());
    }
}
