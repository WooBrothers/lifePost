package com.woobros.member.hub.domain.card;

import org.springframework.data.domain.Page;

public interface CardService {

    CardDto.ReadResponse postCard(CardDto.PostRequest cardPostReqDto);

    CardDto.PageResponse getLatestCard();

    Page<CardDto.PageResponse> getCardsPage(Long lastCardId, int size);
}
