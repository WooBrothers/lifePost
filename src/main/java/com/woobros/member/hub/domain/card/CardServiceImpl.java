package com.woobros.member.hub.domain.card;

import com.woobros.member.hub.common.exception.CommonException;
import com.woobros.member.hub.common.exception.ErrorEnum;
import com.woobros.member.hub.domain.card.CardDto.PageResponse;
import com.woobros.member.hub.domain.card.CardDto.PostCustomRequest;
import com.woobros.member.hub.domain.card.CardDto.PostFocusRequest;
import com.woobros.member.hub.domain.card.CardDto.PostRequest;
import com.woobros.member.hub.domain.card.CardDto.ReadResponse;
import com.woobros.member.hub.model.card.CardMapper;
import com.woobros.member.hub.model.card.CardTypeEnum;
import com.woobros.member.hub.model.card.affr_card.AffirmationCard;
import com.woobros.member.hub.model.card.affr_card.AffirmationCardRepository;
import com.woobros.member.hub.model.card.memb_card.MemberCard;
import com.woobros.member.hub.model.card.memb_card.MemberCardRepository;
import com.woobros.member.hub.model.card.memb_cust_card.MemberCustomCard;
import com.woobros.member.hub.model.card.memb_cust_card.MemberCustomCardRepository;
import com.woobros.member.hub.model.letter.LetterRepository;
import com.woobros.member.hub.model.member.Member;
import com.woobros.member.hub.model.member.MemberRepository;
import com.woobros.member.hub.model.member_letter.MemberLetterRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

        Member member = getMemberByUserDetail(userDetails);

        Page<MemberCard> memberCards = memberCardRepository.findByMemberIdOrderByCreatedAtDesc(
            member.getId(), pageRequest);

        List<PageResponse> pageResponses = separateMemberCardByType(memberCards);

        return new PageImpl<>(pageResponses);
    }

    @Override
    public Page<PageResponse> getMemberCards(int size, Long memberCardId, UserDetails userDetails) {

        PageRequest pageRequest = PageRequest.of(0, size);

        Member member = getMemberByUserDetail(userDetails);

        Page<MemberCard> memberCards = memberCardRepository
            .findByMemberIdAndIdLessThanOrderByCreatedAtDesc(member.getId(), memberCardId,
                pageRequest);

        List<PageResponse> pageResponses = separateMemberCardByType(memberCards);

        return new PageImpl<>(pageResponses);
    }

    @Override
    public Page<PageResponse> getLatestMemberCustomCards(int size, UserDetails userDetails) {

        PageRequest pageRequest = PageRequest.of(0, size);

        Member member = getMemberByUserDetail(userDetails);

        Page<MemberCustomCard> memberCustomCards = memberCustomCardRepository
            .findByMemberIdOrderByCreatedAtDesc(
                member.getId(), pageRequest);

        return new PageImpl<>(
            memberCustomCards.map(card ->
                cardMapper.toMemberPageResponse(card)
                    .setCardId(card.getId())
            )
                .stream()
                .map(response -> response.setType(CardTypeEnum.CUSTOM))
                .collect(
                    Collectors.toList()));
    }

    @Override
    public Page<PageResponse> getMemberCustomCards(int size, Long memberCustomCardId,
        UserDetails userDetails) {

        PageRequest pageRequest = PageRequest.of(0, size);

        Member member = getMemberByUserDetail(userDetails);

        Page<MemberCustomCard> memberCustomCards = memberCustomCardRepository
            .findByMemberIdAndIdLessThanOrderByCreatedAtDesc(
                member.getId(), memberCustomCardId, pageRequest);

        return new PageImpl<>(
            memberCustomCards
                .map(card -> cardMapper.toMemberPageResponse(card)
                    .setCardId(card.getId())
                ).stream()
                .map(response -> response.setType(CardTypeEnum.CUSTOM))
                .collect(
                    Collectors.toList()));
    }

    @Override
    public Page<PageResponse> getLatestFocusCards(int size, UserDetails userDetails) {
        PageRequest pageRequest = PageRequest.of(0, size);

        Member member = getMemberByUserDetail(userDetails);

        Page<MemberCard> memberCards = memberCardRepository
            .findByMemberIdAndFocusOrderByCreatedAtDesc(
                member.getId(), FocusTypeEnum.ATTENTION, pageRequest);

        return new PageImpl<>(separateMemberCardByType(memberCards));
    }

    @Override
    public Page<PageResponse> getFocusCards(int size, Long memberCardId, UserDetails userDetails) {

        PageRequest pageRequest = PageRequest.of(0, size);

        Member member = getMemberByUserDetail(userDetails);

        Page<MemberCard> memberCards = memberCardRepository
            .findByMemberIdAndFocusAndIdLessThanOrderByCreatedAtDesc(
                member.getId(), FocusTypeEnum.ATTENTION, memberCardId, pageRequest
            );

        return new PageImpl<>(separateMemberCardByType(memberCards));
    }

    @Override
    public ReadResponse getCardContents(Long cardId, CardTypeEnum cardTypeEnum,
        UserDetails userDetails) {

        Member member = getMemberByUserDetail(userDetails);

        CardDto.ReadResponse readResponse;

        if (cardTypeEnum.equals(CardTypeEnum.AFFIRMATION)) {

            MemberCard memberCard = memberCardRepository
                .findByMemberIdAndAffirmationCardIdAndType(member.getId(), cardId, cardTypeEnum)
                .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));

            AffirmationCard affirmationCard = memberCard.getAffirmationCard();
            readResponse = cardMapper.toReadResponse(affirmationCard);
            readResponse.setType(CardTypeEnum.AFFIRMATION);

        } else if (cardTypeEnum.equals(CardTypeEnum.CUSTOM)) {

            MemberCard memberCard = memberCardRepository
                .findByMemberIdAndMemberCustomCardIdAndType(member.getId(), cardId, cardTypeEnum)
                .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));

            readResponse = cardMapper.toReadResponse(memberCard.getMemberCustomCard());
            readResponse.setType(CardTypeEnum.CUSTOM);

        } else {
            throw new CommonException(ErrorEnum.CARD_TYPE_INVALID);
        }

        return readResponse;
    }

    @Override
    public ReadResponse postMemberCustomCard(PostCustomRequest memberCustomCardPostDto,
        UserDetails userDetails) {

        Member member = getMemberByUserDetail(userDetails);

        MemberCustomCard memberCustomCard = cardMapper
            .toMemberCustomCardEntity(memberCustomCardPostDto);

        memberCustomCard.setMember(member);

        MemberCard memberCard = MemberCard.builder()
            .member(member)
            .memberCustomCard(memberCustomCard)
            .focus(FocusTypeEnum.NON)
            .type(CardTypeEnum.CUSTOM)
            .build();

        memberCustomCard = memberCustomCardRepository.save(memberCustomCard);
        memberCardRepository.save(memberCard);

        return cardMapper.toReadResponse(memberCustomCard).setType(CardTypeEnum.CUSTOM);
    }

    @Override
    public ReadResponse postFocusCard(PostFocusRequest focusCardRequest, UserDetails userDetails) {
        Member member = getMemberByUserDetail(userDetails);

        MemberCard memberCard;
        ReadResponse response;

        if (focusCardRequest.getType().equals(CardTypeEnum.AFFIRMATION)) {

            memberCard = memberCardRepository.findByMemberIdAndAffirmationCardIdAndType(
                member.getId(), focusCardRequest.getCardId(), focusCardRequest.getType())
                .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));

            response = cardMapper.toReadResponse(memberCard.getAffirmationCard());

        } else if (focusCardRequest.getType().equals(CardTypeEnum.CUSTOM)) {

            memberCard = memberCardRepository.findByMemberIdAndMemberCustomCardIdAndType(
                member.getId(), focusCardRequest.getCardId(), focusCardRequest.getType())
                .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));

            response = cardMapper.toReadResponse(memberCard.getMemberCustomCard());

        } else {
            throw new CommonException(ErrorEnum.CARD_TYPE_INVALID);
        }

        memberCard.setFocus(FocusTypeEnum.ATTENTION);
        memberCardRepository.save(memberCard);

        return response;
    }

    @Override
    public ReadResponse postAffirmationCard(PostRequest cardPostReqDto, UserDetails userDetails) {
        AffirmationCard affirmationCard = cardMapper.toEntity(cardPostReqDto);

        return cardMapper.toReadResponse(
            affirmationCardRepository.save(affirmationCard)
        );
    }

    @Override
    public void deleteFocusCard(CardTypeEnum type, Long cardId, UserDetails userDetails) {
        Member member = getMemberByUserDetail(userDetails);

        MemberCard memberCard;

        if (type.equals(CardTypeEnum.AFFIRMATION)) {
            memberCard = memberCardRepository.findByMemberIdAndAffirmationCardIdAndType(
                member.getId(), cardId, type)
                .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));

        } else if (type.equals(CardTypeEnum.CUSTOM)) {
            memberCard = memberCardRepository.findByMemberIdAndMemberCustomCardIdAndType(
                member.getId(), cardId, type)
                .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));
        } else {
            throw new CommonException(ErrorEnum.CARD_TYPE_INVALID);
        }

        memberCard.setFocus(FocusTypeEnum.NON);
        memberCardRepository.save(memberCard);
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
                AffirmationCard affirmationCard = memberCard.getAffirmationCard();

                PageResponse pageResponse = cardMapper
                    .toMemberPageResponse(affirmationCard)
                    .setType(memberCard.getType())
                    .setCardId(affirmationCard.getId())
                    .setMemberCardId(memberCard.getId());

                pageResponses.add(pageResponse);

            } else if (memberCard.getType().equals(CardTypeEnum.CUSTOM)) {

                MemberCustomCard memberCustomCard = memberCard.getMemberCustomCard();
                PageResponse pageResponse = cardMapper
                    .toMemberPageResponse(memberCustomCard)
                    .setType(memberCard.getType())
                    .setCardId(memberCustomCard.getId())
                    .setMemberCardId(memberCard.getId());

                pageResponses.add(pageResponse);
            }
        }
        return pageResponses;
    }

    private Member getMemberByUserDetail(UserDetails userDetails) {
        return memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
            new CommonException(ErrorEnum.NOT_FOUND));
    }
}
