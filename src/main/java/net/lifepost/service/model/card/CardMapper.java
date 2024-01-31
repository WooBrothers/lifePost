package net.lifepost.service.model.card;

import java.time.LocalDate;
import net.lifepost.service.domain.card.CardDto.PageResponse;
import net.lifepost.service.domain.card.CardDto.PostCustomRequest;
import net.lifepost.service.domain.card.CardDto.PostRequest;
import net.lifepost.service.domain.card.CardDto.ReadResponse;
import net.lifepost.service.model.card.affr_card.AffirmationCard;
import net.lifepost.service.model.card.memb_cust_card.MemberCustomCard;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface CardMapper {

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    AffirmationCard toEntity(PostRequest postRequestDto);

    MemberCustomCard toMemberCustomCardEntity(PostCustomRequest memberCustomCardPostDto);

    PageResponse toMemberPageResponse(AffirmationCard affirmationCard);

    PageResponse toMemberPageResponse(MemberCustomCard memberCustomCard);

    ReadResponse toReadResponse(AffirmationCard affirmationCard);

    ReadResponse toReadResponse(MemberCustomCard memberCustomCard);
}
