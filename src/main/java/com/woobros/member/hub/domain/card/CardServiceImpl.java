package com.woobros.member.hub.domain.card;

import com.woobros.member.hub.common.exception.CommonException;
import com.woobros.member.hub.common.exception.ErrorEnum;
import com.woobros.member.hub.domain.card.CardDto.PageResponse;
import com.woobros.member.hub.domain.card.CardDto.PostFocusRequest;
import com.woobros.member.hub.domain.card.CardDto.PostRequest;
import com.woobros.member.hub.domain.card.CardDto.ReadResponse;
import com.woobros.member.hub.model.card.CardMapper;
import com.woobros.member.hub.model.card.CardTypeEnum;
import com.woobros.member.hub.model.card.affr_card.AffirmationCard;
import com.woobros.member.hub.model.card.affr_card.AffirmationCardRepository;
import com.woobros.member.hub.model.card.memb_card.MemberCard;
import com.woobros.member.hub.model.card.memb_card.MemberCardRepository;
import com.woobros.member.hub.model.card.memb_cust_card.MemberCustomCardRepository;
import com.woobros.member.hub.model.letter.LetterRepository;
import com.woobros.member.hub.model.member.Member;
import com.woobros.member.hub.model.member.MemberRepository;
import com.woobros.member.hub.model.member_letter.MemberLetterRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {

    private final AffirmationCardRepository affirmationCardRepository;
    private final MemberCardRepository memberCardRepository;
    private final MemberCustomCardRepository memberCustomCardRepository;
    private final LetterRepository letterRepository;
    private final MemberRepository memberRepository;
    private final MemberLetterRepository memberLetterRepository;
    private final CardMapper cardMapper;


    /**
     * 멤버가 소유한 카드중 가장 최신의 카드를 size 만큼 조회
     *
     * @param size
     * @param userDetails
     * @return
     */
    @Override
    public Page<PageResponse> getLatestMemberCards(int size, UserDetails userDetails) {

        PageRequest pageRequest = PageRequest.of(0, size);

        Member member = memberRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));

        Page<MemberCard> memberCards = memberCardRepository.findByMemberIdOrderByCreatedAtDesc(
            member.getId(), pageRequest);

        List<PageResponse> pageResponses = separateMemberCardByType(memberCards);

        return new PageImpl<>(pageResponses);
    }

    @Override
    public Page<PageResponse> getMemberCards(int size, Long memberCardId, UserDetails userDetails) {
        PageRequest pageRequest = PageRequest.of(0, size);

        Member member = memberRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));

        Page<MemberCard> memberCards = memberCardRepository
            .findByMemberIdAndIdAfterOrderByCreatedAtDesc(member.getId(), memberCardId,
                pageRequest);

        List<PageResponse> pageResponses = separateMemberCardByType(memberCards);

        return new PageImpl<>(pageResponses);
    }

    /**
     * 조회한 memberCard들을 type 별로 분류하여 알맞는 responseDto로 매핑하고 배열에 담아 리턴
     *
     * @param memberCards
     * @return
     */
    private List<PageResponse> separateMemberCardByType(Page<MemberCard> memberCards) {

        List<PageResponse> pageResponses = new ArrayList<>();

        for (MemberCard memberCard : memberCards) {
            if (memberCard.getType().equals(CardTypeEnum.AFFIRMATION)) {

                PageResponse pageResponse = cardMapper
                    .toMemberPageResponse(memberCard.getAffirmationCard())
                    .setType(memberCard.getType());

                pageResponses.add(pageResponse);

            } else if (memberCard.getType().equals(CardTypeEnum.CUSTOM)) {

                PageResponse pageResponse = cardMapper
                    .toMemberPageResponse(memberCard.getMemberCustomCard())
                    .setType(memberCard.getType());

                pageResponses.add(pageResponse);
            }
        }
        return pageResponses;
    }

    @Override
    public Page<PageResponse> getLatestMemberCustomCards(int size, UserDetails userDetails) {
        return null;
    }

    @Override
    public Page<PageResponse> getMemberCustomCards(int size, Long memberCustomCardId,
        UserDetails userDetails) {
        return null;
    }

    @Override
    public Page<PageResponse> getLatestFocusCards(int size, UserDetails userDetails) {
        return null;
    }

    @Override
    public Page<PageResponse> getFocusCards(int size, Long focusCardId, UserDetails userDetails) {
        return null;
    }

    @Override
    public ReadResponse getCardContents(Long cardId, CardTypeEnum cardTypeEnum,
        UserDetails userDetails) {
        return null;
    }

    @Override
    public ReadResponse postMemberCustomCard(PostRequest memberCardPostDto,
        UserDetails userDetails) {
        return null;
    }

    @Override
    public ReadResponse postFocusCard(PostFocusRequest focusCardRequest, UserDetails userDetails) {
        return null;
    }

    @Override
    public ReadResponse postAffirmationCard(PostRequest cardPostReqDto, UserDetails userDetails) {
        AffirmationCard affirmationCard = cardMapper.toEntity(cardPostReqDto);

        CardDto.ReadResponse resultResponse = cardMapper.toReadResponse(
            affirmationCardRepository.save(affirmationCard)
        );

        // todo letter mapping
        return resultResponse;
    }
}
