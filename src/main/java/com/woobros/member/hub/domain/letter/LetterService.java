package com.woobros.member.hub.domain.letter;

import com.woobros.member.hub.model.letter.LetterDto;
import org.springframework.data.domain.Page;

public interface LetterService {

    LetterDto.Response postLetter(LetterDto.Request letterReqDto);

    LetterDto.Response getLatestLetter();

    Page<LetterDto.PageResponse> getLettersPage(Long lastLetterId, int size);
}
