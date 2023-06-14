package com.woobros.member.hub.domain.letter;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

public interface LetterService {

    LetterDto.ReadResponse postLetter(LetterDto.PostRequest letterReqDto);

    LetterDto.ReadResponse getLatestLetter();

    Page<LetterDto.PageResponse> getLettersPage(Long lastLetterId, int size);

    LetterDto.ReadResponse getTodayLetterContentsByLetterId(Long letterId,
        UserDetails userDetails);

    LetterDto.ReadResponse getLetterContentsByLetterId(Long letterId,
        UserDetails userDetails);

    Page<LetterDto.PageResponse> getHaveLatestLetterPage(int size,
        UserDetails userDetails);

    Page<LetterDto.PageResponse> getHaveLetterPage(Long lastLetterId, int size,
        UserDetails userDetails);

    Page<LetterDto.PageResponse> getDoesNotHaveLatestLetterPage(int size,
        UserDetails userDetails);

    Page<LetterDto.PageResponse> getDoesNotHaveLetterPage(Long lastLetterId, int size,
        UserDetails userDetails);
}
