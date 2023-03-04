package com.wellclaim.member.domain.logout.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class LogoutRequestDto {
    private static final String ENUM_NAME = "LogoutErrorEnum.";

    @NotNull(message = ENUM_NAME + "ID_NOT_NULL")
    private final int id;
}
