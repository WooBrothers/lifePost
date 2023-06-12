package com.woobros.member.hub.domain.letter;

import com.woobros.member.hub.common.exception.CommonException;
import com.woobros.member.hub.common.exception.ErrorEnum;
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
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
     * [admin] 편지 쓰기
     *
     * @param letterReqDto - 작성한 편지의 정보를 담은 dto
     */
    @Override
    public LetterDto.ReadResponse postLetter(LetterDto.PostRequest letterReqDto) {

        Letter letter = letterMapper.toEntity(letterReqDto);

        return letterMapper.toResponseDto(letterRepository.save(letter));
    }

    /**
     * [open] 가장 최근의 편지 정보 조회
     *
     * @return 마지막 편지 정보 조회 (컨텐츠 미포함)
     */
    @Override
    public LetterDto.ReadResponse getLatestLetter() {

        Letter latestLetter = letterRepository.findTopByOrderByCreatedAtDesc().orElseThrow(
            () -> new CommonException(ErrorEnum.NOT_FOUND)
        );

        return letterMapper.toResponseDto(latestLetter);
    }

    /**
     * [open] 편지 리스트 조회 전달받은 letterId 이후의 size 만큼의 편지 정보 조회한다.
     *
     * @param lastLetterId - 해당 편지(미포함) 아이디 이후에 편지들 조회하기 위해 필요
     * @param size         - 가져올 편지의 수
     * @return Page 에 담긴 편지 리스트 리턴
     */
    @Override
    public Page<LetterDto.PageResponse> getLettersPage(Long lastLetterId, int size) {

        PageRequest pageRequest = PageRequest.of(0, size);

        Page<Letter> lettersPage = letterRepository
            .findByIdLessThanOrderByIdDesc(lastLetterId, pageRequest);

        return new PageImpl<>(
            lettersPage.stream()
                .map(letterMapper::toPageResponseDto)
                .collect(Collectors.toList())
        );
    }

    /**
     * [auth] 오늘의 편지 요청 시 처음이면 저장 + 편지 내용 조회
     *
     * @param letterId    - 읽으려는 편지의 아이디
     * @param userDetails - 요청한 멤버 정보
     */
    @Override
    public LetterDto.ReadResponse getTodayLetterContentsByLetterId(Long letterId,
        UserDetails userDetails) {

        Member member = memberRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));

        Letter latestLetter = letterRepository.findTopByOrderByCreatedAtDesc()
            .filter(letter -> letter.getId().equals(letterId))
            .filter(letter -> letter.getCreatedDate().equals(LocalDate.now()))
            .orElseThrow(() -> new CommonException(ErrorEnum.LETTER_REQUEST_INVALID));

        if (doesNotHaveLetterForMember(member.getId(), latestLetter.getId())) {

            saveLetterAndCardToMember(member, latestLetter);
        }

        return letterMapper.toResponseDto(latestLetter);
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

        Member member = memberRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new CommonException(ErrorEnum.STAMP_NOT_ENOUGH));

        Letter letter = letterRepository.findById(letterId)
            .filter(let -> !let.getCreatedDate().equals(LocalDate.now()))
            .orElseThrow(() -> new CommonException(ErrorEnum.LETTER_REQUEST_INVALID));

        if (doesNotHaveLetterForMember(member.getId(), letter.getId())
            && member.getStampCount() > 0) {

            memberRepository.save(member.useStamp());
            saveLetterAndCardToMember(member, letter);
            saveMemberStampUsed(member, letter);
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
                .build());

        // 확언 카드는 편지 하나에 여러개 일 수 있기에 순회하여 멤버 소유 카드에 저장
        List<AffirmationCard> affirmationCards = affirmationCardRepository
            .findByLetterId(letter.getId());

        for (AffirmationCard affirmationCard : affirmationCards) {
            memberCardRepository
                .save(
                    MemberCard.builder()
                        .memberLetter(memberLetter)
                        .affirmationCard(affirmationCard)
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
}
