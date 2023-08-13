package com.woobros.member.hub.domain.letter;

import com.woobros.member.hub.common.Common;
import com.woobros.member.hub.common.exception.CommonException;
import com.woobros.member.hub.common.exception.ErrorEnum;
import com.woobros.member.hub.domain.card.FocusTypeEnum;
import com.woobros.member.hub.domain.letter.LetterDto.PageResponse;
import com.woobros.member.hub.domain.letter.LetterDto.PostFocusRequest;
import com.woobros.member.hub.model.card.CardTypeEnum;
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
import com.woobros.member.hub.model.stamp.Stamp;
import com.woobros.member.hub.model.stamp.StampRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class LetterServiceImpl implements LetterService {

    /* beans */
    private final LetterRepository letterRepository;
    private final MemberRepository memberRepository;
    private final MemberLetterRepository memberLetterRepository;
    private final MemberCardRepository memberCardRepository;
    private final AffirmationCardRepository affirmationCardRepository;
    private final StampRepository stampRepository;

    private final LetterMapper letterMapper;

    /**
     * [open] 가장 최근의 편지 정보 조회
     *
     * @return 마지막 편지 정보 조회 (컨텐츠 미포함)
     */
    @Override
    public LetterDto.ReadResponse getLatestLetter() {

        Letter latestLetter = letterRepository.findByPostDate(LocalDate.now()).orElseGet(
            () -> letterRepository.findTopByOrderByPostDateDesc()
                .orElseThrow(
                    () -> new CommonException(ErrorEnum.NOT_FOUND)
                )
        );

        LetterDto.ReadResponse response = letterMapper.toResponseDto(latestLetter);
        response.setContents(response.getContentWithOutTag());
        return response.setLimitedContents(response.getContents());
    }

    @Override
    public Page<LetterDto.PageResponse> getMyLetterList(int pageNo, int size,
        List<FocusTypeEnum> focusTypeList, UserDetails userDetails) {

        pageNo = Common.verifyPageNo(pageNo);

        Pageable pageable = PageRequest.of(pageNo, size);

        Member member = getMemberByUserDetail(userDetails);

        // 해당 repo 메소드 > entityGraph 처리 : 즉시 로딩 (n+1해결)
        Page<MemberLetter> memberLetters = memberLetterRepository
            .findByMemberIdAndFocusInOrderByCreatedAtDesc(
                member.getId(), focusTypeList, pageable);

        List<PageResponse> pageResponses = new ArrayList<>();
        memberLetters.forEach(memberLetter -> {
                Letter letter = memberLetter.getLetter();
                PageResponse pageResponse = letterMapper.toPageResponseDto(letter)
                    .setLimitedContent(letter.getContents())
                    .setMemberLetterId(memberLetter.getId())
                    .setFocusType(memberLetter.getFocus());
                pageResponses.add(pageResponse);
            }
        );

        return new PageImpl<>(pageResponses, pageable, memberLetters.getTotalElements());
    }

    @Override
    public Page<PageResponse> getMissLetterList(int pageNo, int size,
        UserDetails userDetails) {
        pageNo = Common.verifyPageNo(pageNo);

        Pageable pageable = PageRequest.of(pageNo, size);

        Member member = getMemberByUserDetail(userDetails);

        Page<Letter> letters = letterRepository.findMissLetter(member.getId(), pageable);

        List<PageResponse> pageResponses = getPageResponse(letters);

        return new PageImpl<>(pageResponses, pageable, letters.getTotalElements());
    }

    @Override
    public Page<PageResponse> getAllLetterList(int pageNo, int size,
        UserDetails userDetails) {
        pageNo = Common.verifyPageNo(pageNo);

        Member member = getMemberByUserDetail(userDetails);

        Pageable pageable = PageRequest.of(pageNo, size);

        Page<Letter> letters = letterRepository.findAllByOrderByIdDesc(pageable);

        List<Long> letterIds = letters.stream().map(Letter::getId)
            .collect(Collectors.toList());

        List<MemberLetter> memberLetters = memberLetterRepository
            .findByMemberIdAndLetterIdInOrderByLetterIdDesc(
                member.getId(), letterIds);

        Map<Long, Map<String, Object>> memberLetterMap = new HashMap<>();

        memberLetters.forEach(memberLetter -> {
            Long letterId = memberLetter.getLetter().getId();

            Map<String, Object> memberLetterInfoMap = new HashMap<>();
            memberLetterInfoMap.put("memberLetterId", memberLetter.getId());
            memberLetterInfoMap.put("focusType", memberLetter.getFocus());

            memberLetterMap.put(letterId, memberLetterInfoMap);
        });

        List<PageResponse> pageResponses = getPageResponse(letters);

        pageResponses.forEach(pageResponse -> {
            if (memberLetterMap.containsKey(pageResponse.getId())) {
                pageResponse
                    .setMemberLetterId(
                        (Long) memberLetterMap.get(pageResponse.getId()).get("memberLetterId"))
                    .setFocusType(
                        (FocusTypeEnum) memberLetterMap.get(pageResponse.getId()).get("focusType"));
            }
        });

        return new PageImpl<>(pageResponses, pageable, letters.getTotalElements());
    }

    @Override
    public void postFocusLetter(PostFocusRequest focusCardRequest, UserDetails userDetails) {
        Member member = getMemberByUserDetail(userDetails);

        MemberLetter memberLetter = memberLetterRepository.findByMemberIdAndLetterId(member.getId(),
            focusCardRequest.getLetterId())
            .filter(
                ml -> ml.getId().equals(focusCardRequest.getMemberLetterId()))
            .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));

        memberLetter.focusLetter(focusCardRequest.getFocusType());
    }

    /**
     * [auth] 오늘의 편지가 아닐 때 우표를 사용해서 편지, 카드 획득 + 편지 내용 조회
     *
     * @param letterId    - 읽으려는 편지의 아이디
     * @param userDetails - 요청한 멤버 정보
     */
    @Override
    public LetterDto.ReadResponse getLetterContentsByLetterId(Long letterId,
        UserDetails userDetails) {

        Member member = getMemberByUserDetail(userDetails);

        Letter letter = letterRepository.findById(letterId)
            .orElseThrow(() -> new CommonException(ErrorEnum.LETTER_REQUEST_INVALID));

        if (doesNotHaveLetterForMember(member.getId(), letter.getId())) {
            if (letter.getPostDate().equals(LocalDate.now())) {
                saveLetterAndCardToMember(member, letter);
                saveMemberStampUsed(member, letter);
            } else if (member.getStampCount() > 0) {
                memberRepository.save(member.useStamp());
                saveLetterAndCardToMember(member, letter);
                saveMemberStampUsed(member, letter);
            } else {
                throw new CommonException(ErrorEnum.LETTER_REQUEST_INVALID);
            }
        }
        return letterMapper.toResponseDto(letter);
    }


    /**
     * 멤버가 최초로 편지를 읽으면 편지와 그 편지에 딸린 카드를 멤버가 소유하도록 저장
     *
     * @param member - 요청한 멤버 정보
     * @param letter - 요청한 편지 정보
     */
    private void saveLetterAndCardToMember(Member member, Letter letter) {

        // 멤버 소유 편지에 저장
        MemberLetter memberLetter = memberLetterRepository
            .save(MemberLetter.builder()
                .member(member)
                .letter(letter)
                .focus(FocusTypeEnum.NON)
                .build());

        // 확언 카드는 편지 하나에 여러개 일 수 있기에 순회하여 멤버 소유 카드에 저장
        List<AffirmationCard> affirmationCards = affirmationCardRepository
            .findByLetterId(letter.getId());

        for (AffirmationCard affirmationCard : affirmationCards) {
            memberCardRepository
                .save(
                    MemberCard.builder()
                        .member(member)
                        .memberLetter(memberLetter)
                        .affirmationCard(affirmationCard)
                        .focus(FocusTypeEnum.NON)
                        .type(CardTypeEnum.AFFIRMATION)
                        .build()
                );
        }
    }

    /**
     * 해당 멤버가 편지가 없다면 true 리턴
     *
     * @param memberId - 요청한 멤버의 아이디
     * @param letterId - 읽으려는 편지의 아이디
     */
    private boolean doesNotHaveLetterForMember(Long memberId, Long letterId) {
        return memberLetterRepository.findByMemberIdAndLetterId(memberId, letterId).isEmpty();
    }

    /**
     * 멤버가 우표를 사용한 내역을 저장한다.
     *
     * @param member - 우표를 사용한 멤버
     * @param letter - 읽은 편지 (우표 사용)
     * @return 우표 정보 리턴
     */
    private void saveMemberStampUsed(Member member, Letter letter) {

        MemberLetter memberLetter = memberLetterRepository
            .findByMemberIdAndLetterId(member.getId(), letter.getId())
            .orElseThrow(() -> new CommonException(ErrorEnum.LETTER_REQUEST_INVALID));

        stampRepository.save(Stamp.builder()
            .member(member)
            .memberLetter(memberLetter)
            .action(-1)
            .build());
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


    private List<PageResponse> getPageResponse(Page<Letter> letters) {
        List<PageResponse> pageResponses = new ArrayList<>();

        letters.forEach(letter -> {
                PageResponse pageResponse = letterMapper.toPageResponseDto(letter)
                    .setLimitedContent(letter.getContents());
                pageResponses.add(pageResponse);
            }
        );

        return pageResponses;
    }
}
