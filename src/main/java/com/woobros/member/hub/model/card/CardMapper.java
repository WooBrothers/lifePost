package com.woobros.member.hub.model.card;

import com.woobros.member.hub.domain.card.CardDto;
import com.woobros.member.hub.model.card.affr_card.AffirmationCard;
import com.woobros.member.hub.model.card.memb_card.MemberCard;
import com.woobros.member.hub.model.card.memb_cust_card.MemberCustomCard;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface CardMapper {

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    AffirmationCard toEntity(CardDto.PostRequest postRequestDto);

    CardDto.PageResponse toMemberPageResponse(MemberCard memberCard);

    CardDto.PageResponse toMemberPageResponse(AffirmationCard affirmationCard);

    CardDto.PageResponse toMemberPageResponse(MemberCustomCard memberCustomCard);

    CardDto.PageResponse toMemberLetterAffirmationPageResponse(AffirmationCard affirmationCard);

    CardDto.ReadResponse toReadResponse(AffirmationCard affirmationCard);

    CardDto.ReadResponse toReadResponse(MemberCustomCard memberCustomCard);
}
