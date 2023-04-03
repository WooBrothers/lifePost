package com.woobros.member.domain.modify.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModifyResponseDto {

    private static final String ENUM_NAME = "ModifyErrorEnum.";

    @NotNull(message = ENUM_NAME + "ID_NOT_NULL")
    private final int id;
}
