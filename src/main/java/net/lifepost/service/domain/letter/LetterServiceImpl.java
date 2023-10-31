package net.lifepost.service.domain.letter;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lifepost.service.common.Common;
import net.lifepost.service.common.exception.CommonException;
import net.lifepost.service.common.exception.ErrorEnum;
import net.lifepost.service.domain.card.FocusTypeEnum;
import net.lifepost.service.domain.letter.LetterDto.PageResponse;
import net.lifepost.service.domain.letter.LetterDto.PostFocusRequest;
import net.lifepost.service.model.card.CardTypeEnum;
import net.lifepost.service.model.card.affr_card.AffirmationCard;
import net.lifepost.service.model.card.affr_card.AffirmationCardRepository;
import net.lifepost.service.model.card.memb_card.MemberCard;
import net.lifepost.service.model.card.memb_card.MemberCardRepository;
import net.lifepost.service.model.letter.Letter;
import net.lifepost.service.model.letter.LetterMapper;
import net.lifepost.service.model.letter.LetterRepository;
import net.lifepost.service.model.member.Member;
import net.lifepost.service.model.member.MemberRepository;
import net.lifepost.service.model.member_letter.MemberLetter;
import net.lifepost.service.model.member_letter.MemberLetterRepository;
import net.lifepost.service.model.stamp.Stamp;
import net.lifepost.service.model.stamp.StampRepository;
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
    private final Common common;
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

        Letter latestLetter = getTodayOrLatestLetter();

        LetterDto.ReadResponse response = letterMapper.toResponseDto(latestLetter);
        response.setContents(response.getContentWithOutTag());
        return response.setLimitedContents(response.getContents());
    }

    @Override
    public Page<LetterDto.PageResponse> getMyLetterList(int pageNo, int size,
        List<FocusTypeEnum> focusTypeList, UserDetails userDetails) {

        pageNo = common.verifyPageNo(pageNo);

        Pageable pageable = PageRequest.of(pageNo, size);

        Member member = common.getMemberByUserDetail(userDetails);

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

        pageNo = common.verifyPageNo(pageNo);

        Pageable pageable = PageRequest.of(pageNo, size);

        Member member = common.getMemberByUserDetail(userDetails);

        LocalDate now = LocalDate.now();
        Page<Letter> letters = letterRepository.findMissLetter(now, member.getId(), pageable);

        List<PageResponse> pageResponses = getPageResponse(letters);

        return new PageImpl<>(pageResponses, pageable, letters.getTotalElements());
    }

    @Override
    public Page<PageResponse> getAllLetterList(int pageNo, int size,
        Optional<UserDetails> userDetails) {

        pageNo = common.verifyPageNo(pageNo);

        Pageable pageable = PageRequest.of(pageNo, size);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Page<Letter> letters = letterRepository
            .findByPostDateBeforeOrderByIdDesc(tomorrow, pageable);
//        Page<Letter> letters = letterRepository.findAllByOrderByIdDesc(pageable);

//        List<Long> letterIds = letters.stream().map(Letter::getId)
//            .collect(Collectors.toList());

        List<PageResponse> pageResponses = getPageResponse(letters);

        // 멤버 정보가 있을 경우 조회한 편지와 유저가 소유한 편지 정보를 매핑한다.
//        if (userDetails.isPresent()) {
//            Member member = common.getMemberByUserDetail(userDetails.get());
//
//            // 조회한 편지 리스트 중 멤버가 소유한 편지 조회
//            List<MemberLetter> memberLetters = memberLetterRepository
//                .findByMemberIdAndLetterIdInOrderByLetterIdDesc(
//                    member.getId(), letterIds);
//
//            Map<Long, MemberLetter> memberLetterMap = new HashMap<>();
//
//            // 편지들중 멤버가 소유했다면 정보 매핑 준비
//            memberLetters.forEach(memberLetter -> {
//                Long letterId = memberLetter.getLetter().getId();
//                memberLetterMap.put(letterId, memberLetter);
//            });
//
//            // 멤버 편지의 아이디와 focus 여부를 매핑
//            pageResponses.forEach(pageResponse -> {
//                if (memberLetterMap.containsKey(pageResponse.getId())) {
//                    pageResponse
//                        .setMemberLetterId(
//                            memberLetterMap.get(pageResponse.getId()).getId())
//                        .setFocusType(
//                            memberLetterMap.get(pageResponse.getId()).getFocus());
//                }
//            });
//        }

        return new PageImpl<>(pageResponses, pageable, letters.getTotalElements());
    }

    @Override
    public Page<PageResponse> getIndexLetterList(Long letterId, int size) {
        Pageable pageable = PageRequest.of(0, size);

        Page<Letter> letters;
        if (letterId == 0) {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            letters = letterRepository
                .findByPostDateBeforeOrderByIdDesc(tomorrow, pageable);
        } else {
            letters = letterRepository
                .findByIdLessThanOrderByPostDateDesc(letterId, pageable);
        }

        List<PageResponse> pageResponses = getPageResponse(letters);

        return new PageImpl<>(pageResponses, pageable, letters.getTotalElements());
    }

    @Override
    public void postFocusLetter(PostFocusRequest focusCardRequest, UserDetails userDetails) {
        Member member = common.getMemberByUserDetail(userDetails);

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

        Member member = common.getMemberByUserDetail(userDetails);

        Letter letter = letterRepository.findById(letterId)
            .orElseThrow(() -> new CommonException(ErrorEnum.LETTER_REQUEST_INVALID));

        Letter latestLetter = getTodayOrLatestLetter();

        if (doesNotHaveLetterForMember(member.getId(), letter.getId())) {

            if (letter.getPostDate().equals(LocalDate.now())
                || letter.getId().equals(latestLetter.getId())) {

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
     * [open] 비로그인 편지 읽기 시 제한된 컨텐츠 전달
     *
     * @param letterId - 읽으려는 편지의 아이디
     */
    @Override
    public LetterDto.ReadResponse getLimitedLetterContents(Long letterId) {

        Letter letter = letterRepository.findById(letterId)
            .orElseThrow(() -> new CommonException(ErrorEnum.LETTER_REQUEST_INVALID));

        LetterDto.ReadResponse letterResponse = letterMapper.toResponseDto(letter);
        return letterResponse.setLimitedContentToLogoutMember();
    }

    /**
     * 오늘의 편지 혹은 가장 마지막 편지 조회
     */
    private Letter getTodayOrLatestLetter() {
        LocalDate now = LocalDate.now();
        return letterRepository.findByPostDate(now).orElseGet(
            () -> letterRepository.findFirstByPostDateBeforeOrderByPostDateDesc(now)
                .orElseThrow(
                    () -> new CommonException(ErrorEnum.NOT_FOUND)
                )
        );
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
