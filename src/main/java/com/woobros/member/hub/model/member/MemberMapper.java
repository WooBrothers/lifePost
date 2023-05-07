package com.woobros.member.hub.model.member;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDto.Info toInfo(Member member);

    MemberDto.Info toInfoByOAuth2Info(OAuth2User user);

    MemberDto.Response toResponse(Member member);

    Member toEntity(MemberDto.Request requestDto);
}
