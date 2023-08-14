package com.woobros.member.hub.domain.card;

import com.woobros.member.hub.common.Common;
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
import com.woobros.member.hub.model.letter.Letter;
import com.woobros.member.hub.model.letter.LetterRepository;
import com.woobros.member.hub.model.member.Member;
import com.woobros.member.hub.model.member.MemberRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CardServiceImpl implements CardService {

    /**
     * beans
     */
    private final AffirmationCardRepository affirmationCardRepository;
    private final MemberCardRepository memberCardRepository;
    private final MemberCustomCardRepository memberCustomCardRepository;
    private final MemberRepository memberRepository;
    private final LetterRepository letterRepository;
    private final CardMapper cardMapper;


    /**
     * 입력한 멤버 카드 이후의 최신의 카드를 size 만큼 멤버 카드 조회
     *
     * @param size        조회할 카드 수
     * @param userDetails security 멤버 정보
     * @param focus       focus 여부
     * @param type        card type
     * @return Page 처리된 카드 정보 (컨텐츠 x)
     */
    @Override
    public Page<PageResponse> getMemberCards(int size, int pageNo,
        Optional<FocusTypeEnum> focus, List<CardTypeEnum> type, UserDetails userDetails) {

        pageNo = Common.verifyPageNo(pageNo);

        Pageable pageable = PageRequest.of(pageNo, size);

        Member member = getMemberByUserDetail(userDetails);

        List<FocusTypeEnum> focusList = focus.map(Collections::singletonList)
            .orElseGet(() -> List.of(FocusTypeEnum.ATTENTION, FocusTypeEnum.NON));

        if (type == null || type.isEmpty()) {
            type = List.of(CardTypeEnum.AFFIRMATION, CardTypeEnum.CUSTOM);
        }

        Page<MemberCard> memberCards = memberCardRepository
            .findMemberCardAndRelatedCardAndLetterInfos(member.getId(), type,
                focusList, pageable);

        List<PageResponse> pageResponses = separateMemberCardByType(memberCards);

        return new PageImpl<>(pageResponses, pageable, memberCards.getTotalElements());
    }

    /**
     * 입력한 멤버 커스텀 카드 이후의 size 만큼 멤버 카드 조회
     *
     * @param size        조회할 카드 수
     * @param userDetails security 멤버 정보
     * @return Page 처리된 카드 정보 (컨텐츠 x)
     */
    @Override
    public Page<CardDto.PageResponse> getMemberCustomCards(int size, int pageNo,
        UserDetails userDetails) {

        pageNo = Common.verifyPageNo(pageNo);

        Pageable pageable = PageRequest.of(pageNo, size);

        Member member = getMemberByUserDetail(userDetails);

        Page<MemberCustomCard> memberCustomCards = memberCustomCardRepository
            .findByMemberIdOrderByCreatedAtDesc(
                member.getId(), pageable);

        List<CardDto.PageResponse> pageResponse = memberCustomCards
            .map(card -> cardMapper.toMemberPageResponse(card)
                .setCardId(card.getId())
            ).stream()
            .map(response -> response.setType(CardTypeEnum.CUSTOM))
            .collect(
                Collectors.toList());

        return new PageImpl<>(pageResponse, pageable, memberCustomCards.getTotalElements());

    }

    /**
     * 입력한 멤버 focus 카드 이후의 size 만큼 focus 카드 조회
     *
     * @param size        조회할 카드 수
     * @param userDetails security 멤버 정보
     * @return Page 처리된 카드 정보 (컨텐츠 x)
     */
    @Override
    public Page<PageResponse> getFocusCards(int size, int pageNo, UserDetails userDetails) {

        pageNo = Common.verifyPageNo(pageNo);

        Pageable pageable = PageRequest.of(pageNo, size);

        Member member = getMemberByUserDetail(userDetails);

        Page<MemberCard> memberCards = memberCardRepository
            .findByMemberIdAndFocusOrderByCreatedAtDesc(
                member.getId(), FocusTypeEnum.ATTENTION, pageable
            );

        return new PageImpl<>(separateMemberCardByType(memberCards), pageable,
            memberCards.getTotalElements());
    }

    /**
     * 입력한 카드 아이디와 타입으로 소지한 카드 내용 읽기
     *
     * @param cardId       memberCard or affirmationCard id
     * @param cardTypeEnum 카드 타입
     * @param userDetails  security 멤버 정보
     * @return 카드 정보 (컨텐츠 o)
     */
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

    /**
     * 멤버가 직접 카드 생성하기
     *
     * @param memberCustomCardPostDto 멤버가 직접 생성할 카드 정보
     * @param userDetails             security 멤버 정보
     * @return 생성한 카드의 정보
     */
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

    /**
     * 멤버가 소유한 카드 중 자주 보고싶은 카드를 focus 처리
     *
     * @param focusCardRequest focus 처리를 위한 카드 정보
     * @param userDetails      security 멤버 정보
     * @return focus한 카드 정보
     */
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

    /**
     * 확언 카드 쓰기
     *
     * @param cardPostReqDto 확언 카드를 쓰기위한 카드 정보
     * @param userDetails    security 멤버 정보
     * @return 생성한 카드 정보
     */
    @Override
    public ReadResponse postAffirmationCard(PostRequest cardPostReqDto, UserDetails userDetails) {
        AffirmationCard affirmationCard = cardMapper.toEntity(cardPostReqDto);

        return cardMapper.toReadResponse(
            affirmationCardRepository.save(affirmationCard)
        );
    }

    /**
     * 멤버가 소지한 카드 중 focus 한 멤버카드 focus 해제 처리
     *
     * @param focusCardRequest focus 취소 처리 위한 카드 정보
     * @param userDetails      security 멤버 정보
     */
    @Override
    public void deleteFocusCard(CardDto.PostFocusRequest focusCardRequest,
        UserDetails userDetails) {

        CardTypeEnum cardTypeEnum = focusCardRequest.getType();
        Long cardId = focusCardRequest.getCardId();
        Member member = getMemberByUserDetail(userDetails);

        MemberCard memberCard;

        if (cardTypeEnum.equals(CardTypeEnum.AFFIRMATION)) {
            memberCard = memberCardRepository.findByMemberIdAndAffirmationCardIdAndType(
                member.getId(), cardId, cardTypeEnum)
                .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));

        } else if (cardTypeEnum.equals(CardTypeEnum.CUSTOM)) {
            memberCard = memberCardRepository.findByMemberIdAndMemberCustomCardIdAndType(
                member.getId(), cardId, cardTypeEnum)
                .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));
        } else {
            throw new CommonException(ErrorEnum.CARD_TYPE_INVALID);
        }

        memberCard.setFocus(FocusTypeEnum.NON);
        memberCardRepository.save(memberCard);
    }

    @Override
    public Long postWriteCardCount(CardDto.PostWriteRequest cardWriteReqDto,
        UserDetails userDetails) {

        Member member = getMemberByUserDetail(userDetails);
        MemberCard memberCard = memberCardRepository
            .findByMemberIdAndId(member.getId(), cardWriteReqDto.getMemberCardId())
            .orElseThrow(
                () -> new CommonException(ErrorEnum.NOT_FOUND)
            );

        memberCard.setWriteCount(memberCard.getWriteCount() + cardWriteReqDto.getCount());
        memberCardRepository.save(memberCard);
        return memberCard.getWriteCount();
    }

    @Override
    public Page<CardDto.PageResponse> getCardListAfterCardId(Long memberCardId, int size,
        UserDetails userDetails) {

        if (memberCardId == 0) {
            memberCardId = Long.MAX_VALUE;
        }

        Pageable pageable = PageRequest.of(0, size);
        Member member = getMemberByUserDetail(userDetails);

        Page<MemberCard> memberCards = memberCardRepository
            .findByIdIsLessThanAndMemberIdAndFocusOrderByIdDesc(memberCardId, member.getId(),
                FocusTypeEnum.ATTENTION,
                pageable);

        List<Long> letterIds = memberCards.stream()
            .filter(card -> card.getType().equals(CardTypeEnum.AFFIRMATION))
            .map(MemberCard::getAffirmationCard)
            .map(AffirmationCard::getLetter)
            .map(Letter::getId)
            .distinct()
            .collect(Collectors.toList());

        List<Letter> letters = letterRepository.findByIdIn(letterIds);
        Map<Long, AffirmationCard> affirmationCardMapByLetterIdKey = new HashMap<>();

        memberCards.forEach(card -> {
            if (card.getType().equals(CardTypeEnum.AFFIRMATION)) {
                AffirmationCard affirmationCard = card.getAffirmationCard();
                affirmationCardMapByLetterIdKey
                    .put(affirmationCard.getLetter().getId(), affirmationCard);
            }
        });

        for (Letter letter : letters) {
            AffirmationCard affirmationCard = affirmationCardMapByLetterIdKey.get(letter.getId());
            affirmationCard.setLetter(letter);
        }

        return new PageImpl<>(separateMemberCardByType(memberCards), pageable,
            memberCards.getTotalElements());
    }

    /**
     * 조회한 memberCard들을 type 별로 분류하여 알맞는 responseDto로 매핑하고 배열에 담아 리턴
     *
     * @param memberCards 분류할 멤버 카드
     * @return pageResponse 를 담은 list
     */
    private List<PageResponse> separateMemberCardByType(Page<MemberCard> memberCards) {

        List<PageResponse> pageResponses = new ArrayList<>();

        for (MemberCard memberCard : memberCards) {
            if (memberCard.getType().equals(CardTypeEnum.AFFIRMATION)) {
                AffirmationCard affirmationCard = memberCard.getAffirmationCard();
                Letter letter = affirmationCard.getLetter();

                PageResponse pageResponse = cardMapper
                    .toMemberPageResponse(affirmationCard)
                    .setType(memberCard.getType())
                    .setFocus(memberCard.getFocus())
                    .setCardId(affirmationCard.getId())
                    .setMemberCardId(memberCard.getId())
                    .setLetterId(letter.getId())
                    .setLetterTitle(letter.getTitleByText())
                    .setPostDate(letter.getPostDate());

                pageResponses.add(pageResponse);

            } else if (memberCard.getType().equals(CardTypeEnum.CUSTOM)) {

                MemberCustomCard memberCustomCard = memberCard.getMemberCustomCard();
                PageResponse pageResponse = cardMapper
                    .toMemberPageResponse(memberCustomCard)
                    .setType(memberCard.getType())
                    .setFocus(memberCard.getFocus())
                    .setCardId(memberCustomCard.getId())
                    .setMemberCardId(memberCard.getId())
                    .setPostDate(memberCard.getCreatedAt().toLocalDate());

                pageResponses.add(pageResponse);
            }
        }
        return pageResponses;
    }

    /**
     * userDetail 을 이용해 유저 조회
     *
     * @param userDetails security 멤버 정보
     * @return member entity
     */
    private Member getMemberByUserDetail(UserDetails userDetails) {
        return memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
            new CommonException(ErrorEnum.NOT_FOUND));
    }
}
