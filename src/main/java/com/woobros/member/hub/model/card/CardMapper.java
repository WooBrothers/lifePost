package com.woobros.member.hub.model.card;

import com.woobros.member.hub.model.card.affirmation_card.AffirmationCard;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface CardMapper {

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    AffirmationCard toEntity(CardDto.PostRequest postRequestDto);

    CardDto.PageResponse toFocusPageResponse(AffirmationCard affirmationCard);

    CardDto.PageResponse toMemberPageResponse(AffirmationCard affirmationCard);

    CardDto.PageResponse toMemberLetterAffirmationPageResponse(AffirmationCard affirmationCard);

    CardDto.ReadResponse toReadResponse(AffirmationCard affirmationCard);
}
