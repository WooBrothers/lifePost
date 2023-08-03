package com.woobros.member.hub.domain.letter;

import com.woobros.member.hub.domain.card.FocusTypeEnum;
import com.woobros.member.hub.domain.letter.LetterDto.PageResponse;
import com.woobros.member.hub.domain.letter.LetterDto.PostFocusRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

public interface LetterService {

    LetterDto.ReadResponse postLetter(LetterDto.PostRequest letterReqDto);

    LetterDto.ReadResponse getLatestLetter();

    Page<PageResponse> getMyLetterList(int pageNo, int size, List<FocusTypeEnum> focusTypeList,
        UserDetails userDetails);

    Page<PageResponse> getMissLetterList(int pageNo, int size,
        UserDetails userDetails);

    Page<PageResponse> getAllLetterList(int pageNo, int size, UserDetails userDetails);

    LetterDto.ReadResponse getTodayLetterContentsByLetterId(Long letterId,
        UserDetails userDetails);

    LetterDto.ReadResponse getLetterContentsByLetterId(Long letterId,
        UserDetails userDetails);

    void postFocusLetter(PostFocusRequest focusCardRequest, UserDetails userDetails);
    
}
