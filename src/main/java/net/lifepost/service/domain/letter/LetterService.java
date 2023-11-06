package net.lifepost.service.domain.letter;

import java.util.List;
import net.lifepost.service.domain.card.FocusTypeEnum;
import net.lifepost.service.domain.letter.LetterDto.PageResponse;
import net.lifepost.service.domain.letter.LetterDto.PostFocusRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

public interface LetterService {

    LetterDto.ReadResponse getLatestLetter();

    Page<PageResponse> getMemberLetterList(Long letterId, int size,
        List<FocusTypeEnum> focusTypeEnumList, UserDetails userDetails);

    Page<PageResponse> getIndexLetterList(Long letterId, int size);

    LetterDto.ReadResponse getLetterContentsByLetterId(Long letterId,
        UserDetails userDetails);

    LetterDto.ReadResponse getOpenLetterContents(Long letterId);

    void postFocusLetter(PostFocusRequest focusCardRequest, UserDetails userDetails);

}
