package com.woobros.member.hub.domain.logout.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogoutRequestDto {

    private static final String ENUM_NAME = "LogoutErrorEnum.";

    @NotNull(message = ENUM_NAME + "ID_NOT_NULL")
    private final int id;
}
