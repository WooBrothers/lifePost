package com.woobros.member.hub.business.member;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDto.Info toInfo(Member member);

    MemberDto.Response toResponse(Member member);

    Member toEntity(MemberDto.Request requestDto);
}
