package com.wellclaim.member.domain.modify.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class ModifyResponseDto {
    private static final String ENUM_NAME = "ModifyErrorEnum.";

    @NotNull(message = ENUM_NAME + "ID_NOT_NULL")
    private final int id;
}
