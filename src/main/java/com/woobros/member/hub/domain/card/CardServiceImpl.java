package com.woobros.member.hub.domain.card;

import com.woobros.member.hub.domain.card.CardDto.PageResponse;
import com.woobros.member.hub.model.card.CardMapper;
import com.woobros.member.hub.model.card.affr_card.AffirmationCard;
import com.woobros.member.hub.model.card.affr_card.AffirmationCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {

    private final AffirmationCardRepository affirmationCardRepository;
    private final CardMapper cardMapper;

    @Override
    public CardDto.ReadResponse postCard(CardDto.PostRequest cardPostReqDto) {
        AffirmationCard affirmationCard = cardMapper.toEntity(cardPostReqDto);
        log.debug(cardPostReqDto.toString());

        CardDto.ReadResponse resultResponse = cardMapper.toReadResponse(
            affirmationCardRepository.save(affirmationCard)
        );
        log.debug(resultResponse.toString());

        return resultResponse;
    }

    @Override
    public CardDto.PageResponse getLatestCard() {
        return null;
    }

    @Override
    public Page<PageResponse> getCardsPage(Long lastCardId, int size) {
        return null;
    }

}
