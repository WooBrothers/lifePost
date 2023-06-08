package com.woobros.member.hub.domain.letter;

import com.woobros.member.hub.model.letter.Letter;
import com.woobros.member.hub.model.letter.LetterDto;
import com.woobros.member.hub.model.letter.LetterMapper;
import com.woobros.member.hub.model.letter.LetterRepository;
import com.woobros.member.hub.model.member.Member;
import com.woobros.member.hub.model.member.MemberRepository;
import com.woobros.member.hub.model.member_letter.MemberLetter;
import com.woobros.member.hub.model.member_letter.MemberLetterRepository;
import java.time.LocalDate;
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
    private final LetterMapper letterMapper;

    @Override
    public LetterDto.ReadResponse getLatestLetter() {
        /* 가장 최근의 편지 조회 */

        Letter latestLetter = letterRepository.findTopByOrderByCreatedAtDesc().orElseThrow(
            () -> new RuntimeException("no have any letters.")
        );
        log.debug(latestLetter.toString());

        LetterDto.ReadResponse response = letterMapper.toResponseDto(latestLetter);
        log.debug(response.toString());

        return response;
    }

    @Override
    public Page<LetterDto.PageResponse> getLettersPage(Long lastLetterId, int size) {
        /*  */
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
        log.debug(letter.toString());

        LetterDto.ReadResponse resultResponse = letterMapper
            .toResponseDto(letterRepository.save(letter));
        log.debug(resultResponse.toString());

        return resultResponse;
    }

    @Override
    public LetterDto.ReadResponse getTodayLetterContentsByLetterId(Long letterId,
        UserDetails userDetails) {
        /* 오늘의 편지 요청 시 처음이면 저장 / letter 내용 조회 */

        Member member = memberRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("user not found"));

        Letter latestLetter = letterRepository.findTopByOrderByCreatedAtDesc()
            .filter(letter -> letter.getId().equals(letterId))
            .filter(letter -> letter.getCreatedDate().equals(LocalDate.now()))
            .orElseThrow(() -> new RuntimeException("letter request is invalid."));

        if (memberLetterRepository.findByMemberIdAndLetterId(member.getId(), latestLetter.getId())
            .isEmpty()) {

            memberLetterRepository
                .save(MemberLetter.builder()
                    .member(member)
                    .letter(latestLetter)
                    .build());
        }

        return letterMapper.toResponseDto(latestLetter);
    }

    @Override
    public LetterDto.ReadResponse getLetterContentsByLetterId(Long letterId,
        UserDetails userDetails) {
        return null;
    }
}
