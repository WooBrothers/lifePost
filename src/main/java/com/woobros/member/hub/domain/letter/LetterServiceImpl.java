package com.woobros.member.hub.domain.letter;

import com.woobros.member.hub.model.letter.Letter;
import com.woobros.member.hub.model.letter.LetterDto;
import com.woobros.member.hub.model.letter.LetterMapper;
import com.woobros.member.hub.model.letter.LetterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        Letter latestLetter = letterRepository.findTopByOrderByCreatedAtDesc();
        log.debug(latestLetter.toString());

        LetterDto.Response response = letterMapper.toResponseDto(latestLetter);
        log.debug(response.toString());

        return response;
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
