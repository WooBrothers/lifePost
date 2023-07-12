package com.woobros.member.hub.domain.card;

import com.woobros.member.hub.domain.card.CardDto.PageResponse;
import com.woobros.member.hub.model.card.CardTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

public interface CardService {

//    Page<CardDto.PageResponse> getLatestMemberCards(int size, UserDetails userDetails);

    Page<CardDto.PageResponse> getMemberCards(int size, int pageNo, UserDetails userDetails);

//    Page<CardDto.PageResponse> getLatestMemberCustomCards(int size, UserDetails userDetails);

    Page<PageResponse> getMemberCustomCards(int size, int pageNo, UserDetails userDetails);

//    Page<CardDto.PageResponse> getLatestFocusCards(int size, UserDetails userDetails);

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

    void deleteFocusCard(CardDto.PostFocusRequest focusCardRequest, UserDetails userDetails);
}










