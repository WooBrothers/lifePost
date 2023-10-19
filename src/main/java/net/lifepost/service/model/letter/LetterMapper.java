package net.lifepost.service.model.letter;

import java.time.LocalDate;
import net.lifepost.service.domain.letter.LetterDto.PageResponse;
import net.lifepost.service.domain.letter.LetterDto.PostRequest;
import net.lifepost.service.domain.letter.LetterDto.ReadResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface LetterMapper {

    LetterMapper INSTANCE = Mappers.getMapper(LetterMapper.class);

    Letter toEntity(PostRequest requestDto);

    ReadResponse toResponseDto(Letter letter);

    PostRequest toRequestDto(Letter letter);

    @Mapping(source = "letter.postDate", target = "postDate", dateFormat = "yyyy-MM-dd")
    PageResponse toPageResponseDto(Letter letter);
}
