package com.woobros.member.hub.domain.letter;

import com.woobros.member.hub.model.letter.LetterDto;
import org.springframework.data.domain.Page;

public interface LetterService {

    LetterDto.ReadResponse postLetter(LetterDto.PostRequest letterReqDto);

    LetterDto.ReadResponse getLatestLetter();

    Page<LetterDto.PageResponse> getLettersPage(Long lastLetterId, int size);
}
