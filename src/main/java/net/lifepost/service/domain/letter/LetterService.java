package net.lifepost.service.domain.letter;

import java.util.List;
import net.lifepost.service.domain.card.FocusTypeEnum;
import net.lifepost.service.domain.letter.LetterDto.PageResponse;
import net.lifepost.service.domain.letter.LetterDto.PostFocusRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

public interface LetterService {

    LetterDto.ReadResponse getLatestLetter();

    Page<PageResponse> getMyLetterList(int pageNo, int size, List<FocusTypeEnum> focusTypeList,
        UserDetails userDetails);

    Page<PageResponse> getMissLetterList(int pageNo, int size,
        UserDetails userDetails);

    Page<PageResponse> getAllLetterList(int pageNo, int size, UserDetails userDetails);

    LetterDto.ReadResponse getLetterContentsByLetterId(Long letterId,
        UserDetails userDetails);

    void postFocusLetter(PostFocusRequest focusCardRequest, UserDetails userDetails);

}
