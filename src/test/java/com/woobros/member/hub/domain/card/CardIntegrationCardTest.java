package com.woobros.member.hub.domain.card;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woobros.member.hub.domain.card.CardDto.PostFocusRequest;
import com.woobros.member.hub.model.card.CardMapper;
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
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    @Autowired
    private CardMapper cardMapper;

    private final String authorization = "Authorization";
    private final String tokenType = "Bearer ";

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
                        .focus(FocusTypeEnum.ATTENTION)
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
            .build();
        MemberCustomCard memberCustomCard2 = MemberCustomCard.builder()
            .member(member)
            .contents("member custom card create2")
            .build();
        MemberCustomCard memberCustomCard3 = MemberCustomCard.builder()
            .member(member)
            .contents("member custom card create3")
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

        ResultActions response = mockMvc.perform(get("/api/v1/card/auth/member/3/3")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testGetMemberCards_WhenHaveUserToken_WillOk() throws Exception {
        ResultActions response = mockMvc
            .perform(get("/api/v1/card/auth/member/10/3")
                .contentType(MediaType.APPLICATION_JSON)
                .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    void testGetLatestMemberCustomCards() throws Exception {

        ResultActions response = mockMvc.perform(get("/api/v1/card/auth/custom/1/3")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testGetMemberCustomCards() throws Exception {
        ResultActions response = mockMvc
            .perform(get("/api/v1/card/auth/custom/4/3")
                .contentType(MediaType.APPLICATION_JSON)
                .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testGetLatestFocusCards() throws Exception {
        Page<MemberCard> memberCardPage = memberCardRepository
            .findByMemberIdAndIdLessThanOrderByCreatedAtDesc(
                member.getId(), 15L, PageRequest.of(0, 5));

        memberCardPage
            .forEach(card -> {
                if (card.getType().equals(CardTypeEnum.AFFIRMATION)) {
                    card.setFocus(FocusTypeEnum.ATTENTION);
                    memberCardRepository.save(card);
                }
            });

        ResultActions response = mockMvc.perform(get("/api/v1/card/auth/focus/1/3")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testGetFocusCards() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/card/auth/focus/1/3")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testGetCardContents() throws Exception {

        List<MemberCard> memberCards = memberCardRepository.findByMemberId(member.getId());

        String affirmationCardId = null;
        String memberCustomCardId = null;
        for (MemberCard memberCard : memberCards) {
            if (memberCard.getType().equals(CardTypeEnum.AFFIRMATION)
                && affirmationCardId == null) {
                affirmationCardId = memberCard.getAffirmationCard().getId().toString();
            }
            if (memberCard.getType().equals(CardTypeEnum.CUSTOM) && memberCustomCardId == null) {
                memberCustomCardId = memberCard.getMemberCustomCard().getId().toString();
            }
            if (affirmationCardId != null && memberCustomCardId != null) {
                break;
            }
        }

        ResultActions response = mockMvc
            .perform(get("/api/v1/card/auth/AFFIRMATION/" + affirmationCardId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());

        response = mockMvc.perform(get("/api/v1/card/auth/CUSTOM/" + memberCustomCardId)
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testPostMemberCustomCard() throws Exception {

        Map<String, String> memberCustomCardMap = new HashMap<>();
        memberCustomCardMap.put("contents", "member custom card test create");

        ResultActions response = mockMvc.perform(post("/api/v1/card/auth/member/custom")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken)
            .content(objectMapper.writeValueAsString(memberCustomCardMap)));

        response.andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    void testPostFocusCard_Affirmation() throws Exception {

        CardDto.PostFocusRequest postAffrRequest = new PostFocusRequest();
        postAffrRequest.setCardId(14L);
        postAffrRequest.setType(CardTypeEnum.AFFIRMATION);

        ResultActions response = mockMvc.perform(post("/api/v1/card/auth/focus")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken)
            .content(objectMapper.writeValueAsString(postAffrRequest)));

        response.andDo(print())
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.header().exists("Location"));

        String location = response.andReturn().getResponse().getHeader("Location");

        ResultActions redirect = mockMvc.perform(get(location)
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken)
        )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testPostFocusCard_MemberCustom() throws Exception {

        List<MemberCard> memberCards = memberCardRepository.findByMemberId(member.getId());

        memberCards = memberCards.stream()
            .filter(card -> card.getType().equals(CardTypeEnum.CUSTOM)).collect(
                Collectors.toList());

        CardDto.PostFocusRequest postCustRequest = new PostFocusRequest();
        postCustRequest.setCardId(memberCards.get(0).getMemberCustomCard().getId());
        postCustRequest.setType(CardTypeEnum.CUSTOM);

        ResultActions response = mockMvc.perform(post("/api/v1/card/auth/focus")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken)
            .content(objectMapper.writeValueAsString(postCustRequest)));

        response.andDo(print())
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.header().exists("Location"));

        String location = response.andReturn().getResponse().getHeader("Location");

        ResultActions redirect = mockMvc.perform(get(location)
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken)
        )
            .andDo(print())
            .andExpect(status().isOk());
    }
}
