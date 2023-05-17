package com.woobros.member.hub.model.letter;

import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface LetterMapper {

    LetterMapper INSTANCE = Mappers.getMapper(LetterMapper.class);

    Letter toEntity(LetterDto.Request requestDto);

    LetterDto.Response toResponseDto(Letter letter);

    LetterDto.Request toRequestDto(Letter letter);

    @Mapping(source = "letter.createdDate", target = "createdDate", dateFormat = "yyyy-MM-dd")
    LetterDto.PageResponse toPageResponseDto(Letter letter);
}
