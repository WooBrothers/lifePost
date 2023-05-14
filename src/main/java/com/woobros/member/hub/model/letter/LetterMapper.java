package com.woobros.member.hub.model.letter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LetterMapper {

    LetterMapper INSTANCE = Mappers.getMapper(LetterMapper.class);

    Letter toEntity(LetterDto.Request requestDto);

    LetterDto.Response toResponseDto(Letter letter);

    LetterDto.Request toRequestDto(Letter letter);
}
