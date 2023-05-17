package com.woobros.member.hub.domain.letter;

import com.woobros.member.hub.model.letter.Letter;
import com.woobros.member.hub.model.letter.LetterDto;
import com.woobros.member.hub.model.letter.LetterMapper;
import com.woobros.member.hub.model.letter.LetterRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LetterServiceImpl implements LetterService {

    private final LetterRepository letterRepository;
    private final LetterMapper letterMapper;

    @Override
    public LetterDto.Response getLatestLetter() {
        /* 가장 최근의 편지 조회 */

        Letter latestLetter = letterRepository.findTopByOrderByCreatedAtDesc().orElseThrow(
            () -> new RuntimeException("no have any letters.")
        );
        log.debug(latestLetter.toString());

        LetterDto.Response response = letterMapper.toResponseDto(latestLetter);
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
    public LetterDto.Response postLetter(LetterDto.Request letterReqDto) {
        /* 편지 쓰기 */
        Letter letter = letterMapper.toEntity(letterReqDto);
        log.debug(letter.toString());

        LetterDto.Response resultResponse = letterMapper
            .toResponseDto(letterRepository.save(letter));
        log.debug(resultResponse.toString());

        return resultResponse;
    }
}
