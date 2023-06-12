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

    private final LetterRepository letterRepository;
    private final MemberRepository memberRepository;
    private final MemberLetterRepository memberLetterRepository;
    private final MemberCardRepository memberCardRepository;
    private final AffirmationCardRepository affirmationCardRepository;
    private final LetterMapper letterMapper;

    @Override
    public LetterDto.ReadResponse getLatestLetter() {
        /* 가장 최근의 편지 조회 */

        Letter latestLetter = letterRepository.findTopByOrderByCreatedAtDesc().orElseThrow(
            () -> new CommonException(ErrorEnum.NOT_FOUND)
        );

        LetterDto.ReadResponse response = letterMapper.toResponseDto(latestLetter);

        return response;
    }

    @Override
    public Page<LetterDto.PageResponse> getLettersPage(Long lastLetterId, int size) {
        /* 전달받은 letterId 이후의 size 만큼의 편지 조회 */

        PageRequest pageRequest = PageRequest.of(0, size);

        Page<Letter> lettersPage = letterRepository
            .findByIdLessThanOrderByIdDesc(lastLetterId, pageRequest);

        return new PageImpl<>(
            lettersPage.stream().map(letterMapper::toPageResponseDto).collect(Collectors.toList()));
    }

    @Override
    public LetterDto.ReadResponse postLetter(LetterDto.PostRequest letterReqDto) {
        /* 편지 쓰기 */

        Letter letter = letterMapper.toEntity(letterReqDto);

        LetterDto.ReadResponse resultResponse = letterMapper
            .toResponseDto(letterRepository.save(letter));

        return resultResponse;
    }

    @Override
    public LetterDto.ReadResponse getTodayLetterContentsByLetterId(Long letterId,
        UserDetails userDetails) {
        /* 오늘의 편지 요청 시 처음이면 저장 / letter 내용 조회 */

        Member member = memberRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new CommonException(ErrorEnum.NOT_FOUND));

        Letter latestLetter = letterRepository.findTopByOrderByCreatedAtDesc()
            .filter(letter -> letter.getId().equals(letterId))
            .filter(letter -> letter.getCreatedDate().equals(LocalDate.now()))
            .orElseThrow(() -> new CommonException(ErrorEnum.LETTER_REQUEST_INVALID));

        if (memberLetterRepository.findByMemberIdAndLetterId(member.getId(), latestLetter.getId())
            .isEmpty()) {

            MemberLetter memberLetter = memberLetterRepository
                .save(MemberLetter.builder()
                    .member(member)
                    .letter(latestLetter)
                    .build());

            List<AffirmationCard> affirmationCards = affirmationCardRepository
                .findByLetterId(latestLetter.getId());

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

        return letterMapper.toResponseDto(latestLetter);
    }

    @Override
    public LetterDto.ReadResponse getLetterContentsByLetterId(Long letterId,
        UserDetails userDetails) {
        return null;
    }
}
