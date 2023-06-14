package com.woobros.member.hub.domain.card;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woobros.member.hub.domain.card.CardDto.PostFocusRequest;
import com.woobros.member.hub.domain.card.CardDto.PostRequest;
import com.woobros.member.hub.model.card.affr_card.AffirmationCard;
import com.woobros.member.hub.model.card.affr_card.AffirmationCardRepository;
import com.woobros.member.hub.model.card.memb_card.MemberCard;
import com.woobros.member.hub.model.card.memb_card.MemberCardRepository;
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
    private AffirmationCardRepository affirmationCardRepository;

    private final String authorization = "Authorization";
    private final String tokenType = "Bearer ";
    private final String loginRedirectUrl = "http://localhost/login/page";

    @Value("${test.accessToken}")
    private String testAccessToken;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeAll() throws Exception {

        // given
        Member member = memberRepository.findById(1L).orElseThrow(
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

                cards.forEach(card -> memberCardRepository.save(
                    MemberCard.builder()
                        .affirmationCard(card)
                        .memberLetter(memberLetter)
                        .build()
                ));
            }
        );
    }

    @Test
    void testPostLetter_WhenHaveUserToken_WillForbidden() throws Exception {

    }

    @Test
    void testGetLatestMemberCards() throws Exception {
        ResultActions response = mockMvc.perform(get("/auth/member/{size}")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testGetMemberCards() throws Exception {
        ResultActions response = mockMvc.perform(get("/auth/member/{size}/{memberCardId}")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    void testGetLatestMemberCustomCards() throws Exception {
        ResultActions response = mockMvc.perform(get("/auth/custom/{size}")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    void testGetMemberCustomCards() throws Exception {
        ResultActions response = mockMvc
            .perform(get("/auth/custom/{size}/{memberCustomCardId}")
                .contentType(MediaType.APPLICATION_JSON)
                .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    void testGetLatestFocusCards() throws Exception {
        ResultActions response = mockMvc.perform(get("/auth/focus/{size}")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    void testGetFocusCards() throws Exception {
        ResultActions response = mockMvc.perform(get("/auth/focus/{size}/{focusCardId}")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    void testGetCardContents() throws Exception {
        ResultActions response = mockMvc.perform(get("/auth/{cardId}")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken));

        response.andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    void testPostMemberCustomCard() throws Exception {

        CardDto.PostRequest PostRequest = new PostRequest();
        ResultActions response = mockMvc.perform(post("/auth/member/custom")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken)
            .content(objectMapper.writeValueAsString(PostRequest)));

        response.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testPostFocusCard() throws Exception {

        CardDto.PostFocusRequest PostRequest = new PostFocusRequest();
        ResultActions response = mockMvc.perform(post("/auth/focus")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken)
            .content(objectMapper.writeValueAsString(PostRequest)));

        response.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testPostAffirmationCard() throws Exception {

        CardDto.PostRequest PostRequest = new PostRequest();
        ResultActions response = mockMvc.perform(post("/admin/affirmation")
            .contentType(MediaType.APPLICATION_JSON)
            .header(authorization, tokenType + testAccessToken)
            .content(objectMapper.writeValueAsString(PostRequest)));

        response.andDo(print())
            .andExpect(status().isOk());
    }
}
