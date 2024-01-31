package net.lifepost.service.domain.letter;

import java.time.LocalDate;
import java.util.List;
import net.lifepost.service.domain.card.FocusTypeEnum;
import net.lifepost.service.domain.letter.LetterDto.PageResponse;
import net.lifepost.service.domain.letter.LetterDto.PostFocusRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

public interface LetterService {

    Page<PageResponse> getMemberLetterList(Long letterId, int size,
        List<FocusTypeEnum> focusTypeEnumList, UserDetails userDetails);

    Page<PageResponse> getIndexLetterList(LocalDate postDate, int size);

    LetterDto.ReadResponse getOpenLetterContents(Long letterId);

    void postFocusLetter(PostFocusRequest focusCardRequest, UserDetails userDetails);

}
