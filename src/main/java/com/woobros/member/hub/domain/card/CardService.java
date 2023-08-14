package com.woobros.member.hub.domain.card;

import com.woobros.member.hub.domain.card.CardDto.PageResponse;
import com.woobros.member.hub.model.card.CardTypeEnum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

public interface CardService {


    Page<CardDto.PageResponse> getMemberCards(int size, int pageNo, Optional<FocusTypeEnum> focus,
        List<CardTypeEnum> type, UserDetails userDetails);

    Page<PageResponse> getMemberCustomCards(int size, int pageNo, UserDetails userDetails);

    Page<CardDto.PageResponse> getFocusCards(int size, int pageNo, UserDetails
        userDetails);

    CardDto.ReadResponse getCardContents(Long cardId, CardTypeEnum cardTypeEnum,
        UserDetails userDetails);

    CardDto.ReadResponse postMemberCustomCard(CardDto.PostCustomRequest
        memberCustomCardPostDto,
        UserDetails userDetails);

    CardDto.ReadResponse postFocusCard(CardDto.PostFocusRequest focusCardRequest,
        UserDetails userDetails);

    CardDto.ReadResponse postAffirmationCard(CardDto.PostRequest cardPostReqDto,
        UserDetails userDetails);

    Long postWriteCardCount(CardDto.PostWriteRequest cardWriteReqDto,
        UserDetails userDetails);

    void deleteFocusCard(CardDto.PostFocusRequest focusCardRequest, UserDetails userDetails);

    Page<CardDto.PageResponse> getCardListAfterCardId(Long memberCardId, int size,
        UserDetails userDetails);
}










