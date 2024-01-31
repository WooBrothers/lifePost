package net.lifepost.service.domain.card;

import java.util.List;
import java.util.Optional;
import net.lifepost.service.model.card.CardTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

public interface CardService {


    Page<CardDto.PageResponse> getMemberCards(int size, Long letterId,
        Optional<FocusTypeEnum> focus,
        List<CardTypeEnum> type, UserDetails userDetails);


    CardDto.ReadResponse getCardContents(Long cardId, CardTypeEnum cardTypeEnum,
        UserDetails userDetails);

    CardDto.ReadResponse postMemberCustomCard(CardDto.PostCustomRequest
        memberCustomCardPostDto,
        UserDetails userDetails);

    CardDto.ReadResponse postFocusCard(CardDto.PostFocusRequest focusCardRequest,
        UserDetails userDetails);

    void deleteFocusCard(CardDto.PostFocusRequest focusCardRequest, UserDetails userDetails);

    Page<CardDto.PageResponse> getCardListAfterCardId(Long memberCardId, int size,
        UserDetails userDetails);

    void postCardWrite(CardDto.PostWriteRequest postWriteRequest, UserDetails userDetails);

    void postCardWriteReward(UserDetails userDetails);

    void deleteCustomCard(CardDto.DeleteCustomRequest deleteCustomRequest, UserDetails userDetails);
}










