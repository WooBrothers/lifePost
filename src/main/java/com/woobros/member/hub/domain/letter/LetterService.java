package com.woobros.member.hub.domain.letter;

import com.woobros.member.hub.model.letter.LetterDto;

public interface LetterService {

    LetterDto.Response postLetter(LetterDto.Request letterReqDto);

    LetterDto.Response getLatestLetter();
}
