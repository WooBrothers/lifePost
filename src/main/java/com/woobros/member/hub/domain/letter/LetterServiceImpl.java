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
        log.debug("getLatestLetter method is run.");
        Letter latestLetter = letterRepository.findTopByOrderByCreatedAtDesc();
        return letterMapper.toResponseDto(latestLetter);
    }

    @Override
    public LetterDto.Response postLetter(LetterDto.Request letterReqDto) {
        log.debug("postLetter method is run.");

        Letter letter = letterMapper.toEntity(letterReqDto);
        log.debug(letter.toString());

        return letterMapper.toResponseDto(letterRepository.save(letter));
    }
}
